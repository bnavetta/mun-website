package org.brownmun.web.advisor;

import org.brownmun.web.security.AdvisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import javax.validation.Valid;

/**
 * Handles resetting an advisor's password
 */
@Controller
@RequestMapping("/advisor/reset-password")
public class ResetPasswordController
{
    private final AdvisorService advisorService;

    @Autowired
    public ResetPasswordController(AdvisorService advisorService)
    {
        this.advisorService = advisorService;
    }

    @GetMapping
    public String resetPasswordForm(@RequestParam("token") String token, @ModelAttribute("form") ResetPasswordForm form)
    {
        form.setToken(token);
        return "advisor/reset-password";
    }

    @PostMapping
    public String resetPassword(@Valid @ModelAttribute("form") ResetPasswordForm form, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors())
        {
            return "advisor/reset-password";
        }

        if (!form.getPassword().equals(form.getPasswordConfirm()))
        {
            bindingResult.rejectValue("passwordConfirm", "password.mismatch", "Passwords must match");
            return "advisor/reset-password";
        }

        Optional<String> error = advisorService.resetPassword(form.getToken(), form.getPassword());
        if (error.isPresent())
        {

            bindingResult.reject("", error.get());
            return "advisor/reset-password";
        }

        return "redirect:/yourbusun";
    }
}
