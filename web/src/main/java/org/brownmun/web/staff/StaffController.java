package org.brownmun.web.staff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.brownmun.web.security.StaffUser;
import org.brownmun.web.security.User;

@Controller
@RequestMapping("/staff")
public class StaffController
{
    private static final Logger log = LoggerFactory.getLogger(StaffController.class);

    @GetMapping
    public String staffHome()
    {
        return "staff/home";
    }

    @GetMapping("/login")
    public String loginPage()
    {
        return "staff/login";
    }

    @GetMapping("/user-info")
    @ResponseBody
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<UserInfo> userInfo(@AuthenticationPrincipal User user)
    {
        if (user instanceof StaffUser)
        {
            StaffUser staff = (StaffUser) user;
            return ResponseEntity.ok(UserInfo.fromStaffUser(staff));
        }
        else
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Support HTML5 pushState URLs
     */
    @RequestMapping("/**")
    public String ui()
    {
        return "forward:/staff/";
    }
}
