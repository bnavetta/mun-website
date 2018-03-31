package org.brownmun.web.advisors;

import org.brownmun.core.school.SchoolService;
import org.brownmun.core.school.model.Advisor;
import org.brownmun.core.school.model.School;
import org.brownmun.core.school.model.SchoolApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@Controller
@RequestMapping("/your-mun")
public class DashboardController
{
    private static final Logger log = LoggerFactory.getLogger(DashboardController.class);

    private final SchoolService schoolService;

    @Autowired
    public DashboardController(SchoolService schoolService)
    {
        this.schoolService = schoolService;
    }

    @GetMapping
    public String dashboardHome()
    {
        return "advisor-dashboard/home";
    }

    @PreAuthorize("hasRole('ADVISOR')")
    @GetMapping(value = "/self", produces = "application/json")
    @ResponseBody
    public Advisor getSelf(@AuthenticationPrincipal(expression = "asAdvisor()") Advisor currentUser)
    {
        return currentUser;
    }

    @PreAuthorize("hasRole('ADVISOR')")
    @GetMapping(value = "/school", produces = "application/json")
    @ResponseBody
    public School getSchool(@AuthenticationPrincipal(expression = "asAdvisor()") Advisor currentUser)
    {
        return schoolService.loadSchool(currentUser);
    }

    @PreAuthorize("hasRole('ADVISOR')")
    @PostMapping("/update-application")
    @ResponseBody
    public ResponseEntity<SchoolApplication> updateApplication(@RequestBody SchoolApplication app, @AuthenticationPrincipal(expression = "asAdvisor()") Advisor currentUser)
    {
        schoolService.updateApplication(currentUser, app);
        return ResponseEntity.ok(app);
    }

    /**
     * Support HTML5 pushState URLs
     */
    @RequestMapping({ "/application", "/advisors" })
    public String ui()
    {
        return "forward:/your-mun/";
    }
}
