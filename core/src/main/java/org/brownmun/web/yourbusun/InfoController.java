package org.brownmun.web.yourbusun;

import org.brownmun.model.Advisor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for per-school info pages.
 */
@RequestMapping("/yourbusun")
public class InfoController
{
    @GetMapping("/info")
    public String info(@AuthenticationPrincipal Advisor advisor, Model model)
    {
        model.addAttribute("school", advisor.getSchool());
        return "yourbusun/info";
    }
}
