package org.brownmun.web.staff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/staff")
public class StaffController
{
    private static final Logger log = LoggerFactory.getLogger(StaffController.class);

    @GetMapping
    public String staffHome(@AuthenticationPrincipal Object currentUser)
    {
        log.info("Current user: {}", currentUser);

        return "staff/home";
    }

    @GetMapping("/login")
    public String loginPage()
    {
        return "staff/login";
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
