package org.brownmun.web.advisor;

import org.brownmun.advisor.PasswordResetException;
import org.brownmun.advisor.PasswordResetService;
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
    private final PasswordResetService passwordResetService;

    @Autowired
    public ResetPasswordController(PasswordResetService passwordResetService)
    {
        this.passwordResetService = passwordResetService;
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

        try
        {
            passwordResetService.resetPasswordAndLogin(form.getToken(), form.getPassword());
            return "redirect:/yourbusun";
        }
        catch (PasswordResetException e)
        {
            bindingResult.reject(e.getMessageCode());
            return "advisor/reset-password";
        }
    }
}
