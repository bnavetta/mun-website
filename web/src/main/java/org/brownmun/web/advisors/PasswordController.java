package org.brownmun.web.advisors;

import org.brownmun.core.school.PasswordResetException;
import org.brownmun.core.school.PasswordResetService;
import org.brownmun.core.school.model.Advisor;
import org.brownmun.web.security.ConferenceSecurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Map;

/**
 * Handles advisor passwords, mostly for password resets.
 */
@Controller
@RequestMapping("/your-mun/password")
public class PasswordController
{
    private static final Logger log = LoggerFactory.getLogger(PasswordController.class);

    private final PasswordResetService resetService;

    @Autowired
    public PasswordController(PasswordResetService resetService)
    {
        this.resetService = resetService;
    }

    @GetMapping("/forgot")
    public String forgotPasswordForm()
    {
        return "advisor-dashboard/forgot-password.html";
    }

    @PostMapping("/forgot")
    public ModelAndView submitForgotPasswordForm(@RequestParam String email)
    {
        try
        {
            resetService.requestPasswordReset(email);

            return new ModelAndView("advisor-dashboard/forgot-password.html", Map.of(
                    "email", email,
                    "message", "Please check your email for a password reset link"
            ));
        }
        catch (PasswordResetException e)
        {
            log.error("Failed to request password reset", e);
            return new ModelAndView("advisor-dashboard/forgot-password.html", Map.of(
                    "email", email,
                    "error", e.getMessage()
            ));
        }
    }

    @GetMapping("/reset")
    public String passwordResetForm(@RequestParam("token") String token, @ModelAttribute("form") PasswordResetForm form)
    {
        form.setToken(token);
        return "advisor-dashboard/reset-password.html";
    }

    @PostMapping("/reset")
    public String submitPasswordReset(@Valid @ModelAttribute("form") PasswordResetForm form, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors())
        {
            return "advisor-dashboard/reset-password.html";
        }

        if (!form.getPassword().equals(form.getPasswordConfirm()))
        {
            bindingResult.rejectValue("passwordConfirm", "password.mismatch", "Passwords must match");
            return "advisor-dashboard/reset-password.html";
        }

        try
        {
            Advisor advisor = resetService.resetPassword(form.getToken(), form.getPassword());
            ConferenceSecurity.authenticateAsAdvisor(advisor);
            return "redirect:/your-mun";
        }
        catch (PasswordResetException e)
        {
            bindingResult.reject("password.reset.fail", e.getMessage());
            return "advisor-dashboard/reset-password.html";
        }
    }
}
