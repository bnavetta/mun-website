package org.brownmun.web.api;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.brownmun.core.committee.CommitteeService;
import org.brownmun.web.common.DelegateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonView;

import org.brownmun.core.api.Views;
import org.brownmun.core.school.AdvisorService;
import org.brownmun.core.school.SchoolService;
import org.brownmun.core.school.model.Advisor;
import org.brownmun.core.school.model.School;
import org.brownmun.core.school.model.SupplementalInfo;
import org.brownmun.web.security.ConferenceSecurity;

/**
 * API for the staff dashboard
 */
@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('STAFF')")
public class StaffApiController
{
    private final SchoolService schools;
    private final AdvisorService advisors;
    private final CommitteeService committees;

    @Autowired
    public StaffApiController(SchoolService schools, AdvisorService advisors, CommitteeService committees)
    {
        this.schools = schools;
        this.advisors = advisors;
        this.committees = committees;
    }

    @JsonView(Views.Staff.class)
    @GetMapping("/school")
    public List<School> listSchools()
    {
        return schools.listSchools();
    }

    @JsonView(Views.Staff.class)
    @GetMapping("/school/{id}/advisors")
    public List<Advisor> getAdvisors(@PathVariable Long id)
    {
        return schools.getAdvisors(id);
    }

    @JsonView(Views.Staff.class)
    @GetMapping("/school/{id}/supplemental-info")
    public SupplementalInfo getSupplementalInfo(@PathVariable Long id)
    {
        return schools.getSupplementalInfo(id);
    }

    @GetMapping("/school/{id}/delegates")
    public List<DelegateDTO> getDelegates(@PathVariable Long id)
    {
        return schools.getDelegates(id)
                .stream()
                .sorted(Comparator.comparing(d -> d.getPosition().getName()))
                .map(d -> DelegateDTO.create(d.getId(), d.getName(), d.getPosition().getName(),
                        committees.getFullName(d.getPosition().getCommittee())))
                .collect(Collectors.toList());
    }

    @JsonView(Views.Staff.class)
    @GetMapping("/advisors")
    public List<Advisor> listAdvisors()
    {
        return schools.listAdvisors();
    }

    @PutMapping("/advisors/authenticate-as")
    public ResponseEntity<Void> authenticateAsAdvisor(@RequestParam Long advisorId)
    {
        Optional<Advisor> advisorOpt = advisors.getAdvisor(advisorId);
        if (advisorOpt.isPresent())
        {
            ConferenceSecurity.authenticateAsAdvisor(advisorOpt.get());
            return ResponseEntity.ok().build();
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }
}
