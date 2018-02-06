package org.brownmun.web.admin.attendance;

import org.brownmun.model.Attendance;
import org.brownmun.model.committee.Committee;
import org.brownmun.model.committee.Position;
import org.brownmun.model.repo.CommitteeRepository;
import org.brownmun.model.repo.PositionRepository;
import org.brownmun.web.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/committee/{id}/attendance")
public class AttendanceController
{
    private final CommitteeRepository committeeRepo;
    private final PositionRepository positionRepo;

    @Autowired
    public AttendanceController(CommitteeRepository committeeRepo, PositionRepository positionRepo)
    {
        this.committeeRepo = committeeRepo;
        this.positionRepo = positionRepo;
    }

    @ModelAttribute("committee")
    public Committee getCommittee(@PathVariable("id") long id)
    {
        return committeeRepo.findOne(id);
    }

    @GetMapping(name = "AC#attendanceIndex")
    public String index()
    {
        return "admin/attendance/index";
    }

    @GetMapping(produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<PositionAttendance>> getAttendance(@ModelAttribute Committee committee)
    {
        if (committee == null)
        {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(committee.getPositions().stream()
                .filter(p -> p.getDelegate() != null).map(p -> {
            PositionAttendance dto = new PositionAttendance();
            dto.setSchoolName(p.getDelegate().getSchool().getName());
            dto.setPositionId(p.getId());
            dto.setPositionName(p.getName());
            dto.setPositionPaper(p.getAttendance().isPositionPaperSubmitted());
            dto.setSessionOne(p.getAttendance().isPresentForSession(1));
            dto.setSessionTwo(p.getAttendance().isPresentForSession(2));
            dto.setSessionThree(p.getAttendance().isPresentForSession(3));
            dto.setSessionFour(p.getAttendance().isPresentForSession(4));
            return dto;
        }).sorted(Comparator.comparing(PositionAttendance::getPositionName)).collect(Collectors.toList()));
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<ApiResponse> submitAttendance(@RequestBody PositionAttendance request)
    {
        Position position = positionRepo.findOne(request.getPositionId());
        if (position == null)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Position not found"));
        }

        Attendance a = position.getAttendance();
        a.setPositionPaperSubmitted(request.isPositionPaper());
        a.setPresentForSession(1, request.isSessionOne());
        a.setPresentForSession(2, request.isSessionTwo());
        a.setPresentForSession(3, request.isSessionThree());
        a.setPresentForSession(4, request.isSessionFour());
        positionRepo.save(position);

        return ResponseEntity.ok(new ApiResponse(true, "Attendance updated"));
    }
}
