package org.brownmun.web.yourmun;

import com.google.common.collect.ImmutableMap;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.brownmun.advisor.AdvisorCreationException;
import org.brownmun.advisor.AdvisorCreationRequest;
import org.brownmun.advisor.AdvisorCreationService;
import org.brownmun.mail.MailException;
import org.brownmun.model.School;
import org.brownmun.model.advisor.Advisor;
import org.brownmun.web.security.AdvisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Handle creating non-primary school advisors
 */
@Slf4j
@Controller
@RequestMapping("/yourmun/add-advisors")
public class AddAdvisorController
{
    private final AdvisorService advisorService;
    private final AdvisorCreationService advisorCreationService;
    private final MessageSource messages;

    @Autowired
    public AddAdvisorController(AdvisorService advisorService, AdvisorCreationService advisorCreationService, MessageSource messages)
    {
        this.advisorService = advisorService;
        this.advisorCreationService = advisorCreationService;
        this.messages = messages;
    }

    @GetMapping
    public String addAdvisors(@AuthenticationPrincipal Advisor advisor, Model model)
    {
        School school = advisorService.load(advisor).getSchool();
        model.addAttribute("school", school);
        return "yourmun/add-advisors";
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Map<String, Object>> submitAdvisors(@AuthenticationPrincipal Advisor advisor, @RequestBody AddAdvisorsRequest request, Locale locale) throws MailException
    {
        advisor = advisorService.load(advisor);
        School school = advisor.getSchool();
        if (!school.getId().equals(request.getSchoolId()))
        {
            return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ImmutableMap.of("error", "Incorrect school ID"));
        }

        try
        {
            advisorCreationService.inviteAdvisors(school, request.getAdvisors());
            return ResponseEntity.ok(ImmutableMap.of("success", true));
        }
        catch (AdvisorCreationException e)
        {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ImmutableMap.of("error", messages.getMessage(e.getMessageCode(), null, locale)));
        }
    }

    @GetMapping("/confirm")
    public String createAdvisorForm(@RequestParam("token") String token, Model model)
    {
        AdvisorRegistrationForm form = new AdvisorRegistrationForm();
        form.setToken(token);
        model.addAttribute("registration", form);
        return "yourmun/confirm-advisor";
    }

    @PostMapping("/confirm")
    public String createAdvisor(@Valid @ModelAttribute("registration") AdvisorRegistrationForm form, BindingResult bindingResult, Model model)
    {
        if (bindingResult.hasErrors())
        {
            return "yourmun/confirm-advisor";
        }

        // Not always getting checked for some reason?
        if (!form.passwordsMatch())
        {
            model.addAttribute("error", "Passwords do not match");
            return "yourmun/confirm-advisor";
        }

        Advisor base = new Advisor();
        base.setPhoneNumber(form.getPhoneNumber());
        try
        {
            Advisor created = advisorCreationService.createAdvisorAndLogin(form.getToken(), form.getPassword(), base);
            return "redirect:/yourmun/";
        }
        catch (AdvisorCreationException e)
        {
            model.addAttribute("errorCode", e.getMessageCode());
            return "yourmun/confirm-advisor";
        }
    }

    /**
     * JSON request to add advisors.
     */
    @Data
    public static class AddAdvisorsRequest
    {
        private Long schoolId;
        private List<AdvisorCreationRequest> advisors;
    }
}
