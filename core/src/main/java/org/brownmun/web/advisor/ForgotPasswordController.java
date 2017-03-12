package org.brownmun.web.advisor;

import org.brownmun.web.security.AdvisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/**
 * Handles password reset requests
 */
@Controller
@RequestMapping("/advisor/forgot-password")
public class ForgotPasswordController
{
    private final AdvisorService advisorService;

    @Autowired
    public ForgotPasswordController(AdvisorService advisorService)
    {
        this.advisorService = advisorService;
    }

    @GetMapping
    public String forgotPasswordForm()
    {
        return "advisor/forgot-password";
    }

    @PostMapping
    public String forgotPassword(@RequestParam("email") String email, Model model)
    {
        model.addAttribute("email", email);
        Optional<String> error = advisorService.requestPasswordReset(email);
        if (error.isPresent())
        {
            model.addAttribute("error", error.get());
        }
        else
        {
            model.addAttribute("message", "Check your email for a password reset link");
        }

        return "advisor/forgot-password";
    }
}
