package org.brownmun.web.advisors;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.google.common.base.Strings;

import org.brownmun.core.school.AdvisorService;
import org.brownmun.core.school.SchoolService;
import org.brownmun.core.school.model.Advisor;
import org.brownmun.core.school.model.School;
import org.brownmun.core.school.model.SchoolApplication;
import org.brownmun.core.school.model.SupplementalInfo;

@Controller
@RequestMapping("/your-mun")
public class DashboardController
{
    private static final Logger log = LoggerFactory.getLogger(DashboardController.class);

    private final SchoolService schoolService;
    private final AdvisorService advisorService;

    @Autowired
    public DashboardController(SchoolService schoolService, AdvisorService advisorService)
    {
        this.schoolService = schoolService;
        this.advisorService = advisorService;
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
    public ResponseEntity<SchoolApplication> updateApplication(@RequestBody SchoolApplication app,
            @AuthenticationPrincipal(expression = "asAdvisor()") Advisor currentUser)
    {
        schoolService.updateApplication(currentUser, app);
        return ResponseEntity.ok(app);
    }

    @PreAuthorize("hasRole('ADVISOR')")
    @GetMapping("/supplemental-info")
    @ResponseBody
    public SupplementalInfo getSupplementalInfo(
            @AuthenticationPrincipal(expression = "asAdvisor()") Advisor currentUser)
    {
        return schoolService.getSupplementalInfo(currentUser.getSchoolId());
    }

    @PreAuthorize("hasRole('ADVISOR')")
    @PostMapping("/supplemental-info")
    @ResponseBody
    public ResponseEntity<SupplementalInfo> updateSupplementalInfo(
            @AuthenticationPrincipal(expression = "asAdvisor()") Advisor currentUser,
            @RequestBody SupplementalInfo info)
    {
        info = schoolService.updateSupplementalInfo(currentUser, info);
        return ResponseEntity.ok(info);
    }

    /**
     * Handler for an advisor to change their password. The request body is a JSON
     * object with two keys, {@code password} and {@code confirm}, which are the
     * advisor's new password and the confirmation of that.
     */
    @PreAuthorize("hasRole('ADVISOR')")
    @PostMapping("/change-password")
    @ResponseBody
    public ResponseEntity<String> changePassword(
            @AuthenticationPrincipal(expression = "asAdvisor()") Advisor currentUser,
            @RequestBody Map<String, String> passInfo)
    {
        String password = passInfo.get("password");
        if (Strings.isNullOrEmpty(password))
        {
            return ResponseEntity.badRequest().body("Missing password");
        }

        String confirm = passInfo.get("confirm");
        if (Strings.isNullOrEmpty(confirm))
        {
            return ResponseEntity.badRequest().body("Missing password confirmation");
        }

        if (!password.equals(confirm))
        {
            return ResponseEntity.badRequest().body("Passwords don't match");
        }

        advisorService.changePassword(currentUser, password);

        return ResponseEntity.ok("Password updated");
    }

    /**
     * Support HTML5 pushState URLs
     */
    @GetMapping({ "/application", "/advisors", "/supplemental", "/change-password" })
    public String ui()
    {
        return "forward:/your-mun/";
    }
}
