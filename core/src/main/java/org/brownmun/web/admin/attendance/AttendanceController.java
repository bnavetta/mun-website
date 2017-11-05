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

    @GetMapping
    public String index()
    {
        return "admin/attendance/index";
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<ApiResponse> submitAttendance(@RequestBody AttendanceRequest request)
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
