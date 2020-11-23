package org.brownmun.web.api;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.base.Strings;

import org.brownmun.core.api.Views;
import org.brownmun.core.award.AwardService;
import org.brownmun.core.award.model.Award;
import org.brownmun.core.committee.CommitteeService;
import org.brownmun.core.committee.model.Position;
import org.brownmun.core.school.AdvisorService;
import org.brownmun.core.school.SchoolService;
import org.brownmun.core.school.model.Advisor;
import org.brownmun.core.school.model.Delegate;
import org.brownmun.core.school.model.School;
import org.brownmun.core.school.model.SupplementalInfo;
import org.brownmun.web.common.DelegateDTO;
import org.brownmun.web.security.ConferenceSecurity;

/**
 * API for the staff dashboard
 */
@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('STAFF')")
public class StaffApiController {
    private static final Logger log = LoggerFactory.getLogger(StaffApiController.class);

    private final SchoolService schools;
    private final AdvisorService advisors;
    private final CommitteeService committees;
    private final AwardService awards;

    @Autowired
    public StaffApiController(SchoolService schools, AdvisorService advisors, CommitteeService committees,
            AwardService awards) {
        this.schools = schools;
        this.advisors = advisors;
        this.committees = committees;
        this.awards = awards;
    }

    @JsonView(Views.Staff.class)
    @GetMapping("/school")
    public List<School> listSchools() {
        return schools.listSchools();
    }

    @JsonView(Views.Staff.class)
    @GetMapping("/school/{id}/advisors")
    public List<Advisor> getAdvisors(@PathVariable Long id) {
        return schools.getAdvisors(id);
    }

    @JsonView(Views.Staff.class)
    @GetMapping("/school/{id}/supplemental-info")
    public SupplementalInfo getSupplementalInfo(@PathVariable Long id) {
        return schools.getSupplementalInfo(id);
    }

    @GetMapping("/school/{id}/delegates")
    public List<DelegateDTO> getDelegates(@PathVariable Long id) {
        return schools.getDelegates(id).stream().sorted(Comparator.comparing(d -> d.getPosition().getName()))
                .map(d -> DelegateDTO.create(d.getId(), d.getName(), d.getPosition().getName(),
                        committees.getFullName(d.getPosition().getCommittee()), d.getGatherlyLink(),
                        d.getPosition().getId()))
                .collect(Collectors.toList());
    }

    @JsonView(Views.Staff.class)
    @GetMapping("/advisors")
    public List<Advisor> listAdvisors() {
        return schools.listAdvisors();
    }

    @PutMapping("/advisors/authenticate-as")
    public ResponseEntity<Void> authenticateAsAdvisor(@RequestParam Long advisorId) {
        Optional<Advisor> advisorOpt = advisors.getAdvisor(advisorId);
        if (advisorOpt.isPresent()) {
            ConferenceSecurity.authenticateAsAdvisor(advisorOpt.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/committee/{id}/positions")
    public ResponseEntity<List<PositionInfo>> committeePositions(@PathVariable long id) {
        return committees.getCommittee(id)
                .map(committee -> ResponseEntity.ok(committees.getPositions(committee).stream().map(position -> {
                    Delegate delegate = position.getDelegate();
                    if (delegate != null) {
                        return PositionInfo.create(position.getId(), position.getName(),
                                Strings.nullToEmpty(delegate.getName()),
                                schools.getSchool(delegate.getSchool().getId()).get().getName(),
                                delegate.getAttendance().isPositionPaperSubmitted(),
                                delegate.getAttendance().isSessionOne(), delegate.getAttendance().isSessionTwo(),
                                delegate.getAttendance().isSessionThree(), delegate.getAttendance().isSessionFour(),
                                delegate.getAttendance().isSessionFive());
                    } else {
                        return PositionInfo.create(position.getId(), position.getName(), "", "", false, false, false,
                                false, false, false);
                    }
                }).sorted(Comparator.comparing(PositionInfo::name)).collect(Collectors.toList())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/committee/{id}/attendance")
    @PreAuthorize("principal.canAccessCommittee(id)")
    public ResponseEntity<Void> submitAttendance(@PathVariable("id") long committeeId,
            @RequestBody AttendanceRequest request) {
        Optional<Position> posOption = committees.getPosition(request.positionId());
        if (!posOption.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Position position = posOption.get();
        if (position.getCommittee().getId() != committeeId) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Delegate delegate = position.getDelegate();
        if (delegate == null) {
            return ResponseEntity.badRequest().build();
        }

        switch (request.session()) {
            case "positionPaper":
                delegate.getAttendance().setPositionPaperSubmitted(request.present());
                break;
            case "session1":
                delegate.getAttendance().setSessionOne(request.present());
                break;
            case "session2":
                delegate.getAttendance().setSessionTwo(request.present());
                break;
            case "session3":
                delegate.getAttendance().setSessionThree(request.present());
                break;
            case "session4":
                delegate.getAttendance().setSessionFour(request.present());
                break;
            case "session5":
                delegate.getAttendance().setSessionFive(request.present());
                break;
            default:
                log.warn("Invalid session {} when trying to mark attendance", request.session());
                return ResponseEntity.badRequest().build();
        }

        schools.saveDelegate(delegate);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/committee/{id}/awards")
    @PreAuthorize("principal.canAccessCommittee(id)")
    public ResponseEntity<List<CommitteeAward>> awards(@PathVariable long id) {
        try {
            return ResponseEntity.ok(awards
                    .awardsForCommittee(id).stream().map(
                            award -> CommitteeAward.create(award.getId(), award.getType(),
                                    award.getPosition() == null ? OptionalLong.empty()
                                            : OptionalLong.of(award.getPosition().getId())))
                    .collect(Collectors.toList()));
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Error listing awards for {}: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/committee/{id}/awards")
    @PreAuthorize("principal.canAccessCommittee(id)")
    public ResponseEntity<CommitteeAward> submitAward(@PathVariable long id, @RequestBody CommitteeAward award) {
        Optional<Award> awardOpt = awards.getAward(award.id());
        if (!awardOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        if (awardOpt.get().getCommittee().getId() != id) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            if (!award.positionId().isPresent()) {
                Award a = awards.unassignAward(award.id());
                return ResponseEntity.ok(CommitteeAward.create(a.getId(), a.getType(), OptionalLong.empty()));
            } else {
                Award a = awards.setAward(award.id(), award.positionId().getAsLong());
                return ResponseEntity.ok(CommitteeAward.create(a.getId(), a.getType(),
                        a.getPosition() == null ? OptionalLong.empty() : OptionalLong.of(a.getPosition().getId())));
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Error giving award {} ({}) to {}: {}", award.id(), award.type(), award.positionId(), e);
            return ResponseEntity.badRequest().build();
        }
    }
}
