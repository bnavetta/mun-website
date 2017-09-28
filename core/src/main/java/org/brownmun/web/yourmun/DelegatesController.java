package org.brownmun.web.yourmun;

import org.brownmun.model.School;
import org.brownmun.model.advisor.Advisor;
import org.brownmun.web.security.AdvisorService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/yourmun/delegates")
public class DelegatesController
{
    private final AdvisorService advisorService;

    public DelegatesController(AdvisorService advisorService)
    {
        this.advisorService = advisorService;
    }

    @GetMapping("/")
    public String delegates(@AuthenticationPrincipal Advisor advisor, Model model)
    {
        advisor = advisorService.load(advisor);
        School school = advisor.getSchool();

        model.addAttribute("delegates", school.getDelegates());

        return "yourmun/delegates";
    }
}
