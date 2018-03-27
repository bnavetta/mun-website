package org.brownmun.web.advisors;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/your-mun")
public class DashboardController
{
    @GetMapping
    public String dashboardHome()
    {
        return "advisor-dashboard/home";
    }
}
