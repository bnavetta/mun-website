package org.brownmun.web.advisor;

import org.brownmun.advisor.PasswordResetException;
import org.brownmun.advisor.PasswordResetService;
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
    private final PasswordResetService passwordResetService;

    @Autowired
    public ForgotPasswordController(PasswordResetService passwordResetService)
    {
        this.passwordResetService = passwordResetService;
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
        try
        {
            passwordResetService.requestPasswordReset(email);
            model.addAttribute("message", "Check your email for a password reset link");
        }
        catch (PasswordResetException e)
        {
            model.addAttribute("errorCode", e.getMessageCode());
        }

        return "advisor/forgot-password";
    }
}
