package org.brownmun.web.yourbusun;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.brownmun.mail.EmailDescriptor;
import org.brownmun.mail.MailException;
import org.brownmun.mail.MailService;
import org.brownmun.mail.MessageLoader;
import org.brownmun.model.Advisor;
import org.brownmun.model.NewAdvisorToken;
import org.brownmun.model.School;
import org.brownmun.model.repo.SchoolRepository;
import org.brownmun.web.security.AdvisorRequest;
import org.brownmun.web.security.AdvisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.validation.Valid;

/**
 * Handle creating non-primary school advisors
 */
@Slf4j
@Controller
@RequestMapping("/yourbusun/add-advisors")
public class AddAdvisorController
{
    private final AdvisorService advisorService;
    private final MailService mailService;
    private final MessageLoader messageLoader;

    @Autowired
    public AddAdvisorController(AdvisorService advisorService, MailService mailService, MessageLoader messageLoader)
    {
        this.advisorService = advisorService;
        this.mailService = mailService;
        this.messageLoader = messageLoader;
    }

    /*
       Flow:
       1. Existing advisor goes to form for adding advisors (addAdvisors)
                REACT!!!
       2. Fills out each one's name and email address (addAdvisors / submitAdvisors)
       3. An NewAdvisorToken is generated for each (submitAdvisors)
       4. Each advisor is emailed their token
       5. When they get the email, they follow a link to create their account (createAdvisor)
     */

    @GetMapping
    public String addAdvisors(@AuthenticationPrincipal Advisor advisor, Model model)
    {
        model.addAttribute("school", advisorService.load(advisor).getSchool());
        return "yourbusun/add-advisors";
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Map<String, Object>> submitAdvisors(@AuthenticationPrincipal Advisor advisor, @RequestBody AddAdvisorsRequest request) throws MailException
    {
        advisor = advisorService.load(advisor);
        School school = advisor.getSchool();
        if (!school.getId().equals(request.getSchoolId()))
        {
            return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ImmutableMap.of("error", "Incorrect school ID"));
        }

        Iterable<NewAdvisorToken> tokens = advisorService.createTokens(school, request.getAdvisors());
        Map<String, Map<String, String>> recipients = Maps.newHashMap();
        for (NewAdvisorToken token : tokens)
        {
            Map<String, String> variables = Maps.newHashMap();
            variables.put("name", token.getAdvisorName());
            variables.put("school", school.getName());
            // TODO: put base URI of site in ConferenceProperties
            // and use a URI builder
            variables.put("registerUrl", "http://localhost:8080/yourbusun/add-advisors/confirm?token=" + token.getToken());
            recipients.put(token.getAdvisorEmail(), variables);
        }

        // TODO: pull out constants
        EmailDescriptor message = new EmailDescriptor();
        message.setFrom("admin@busun.org");
        message.setReplyTo(Optional.of("technology@busun.org"));
        message.setRecipients(recipients);
        message.setSubject("Create a BUSUN Account");
        message.setHtml(messageLoader.getMessage("new-advisor.html"));
        mailService.send(message);

        return ResponseEntity.ok(ImmutableMap.of("success", true));
    }

    @GetMapping("/confirm")
    public String createAdvisorForm(@RequestParam("token") String token, Model model)
    {
        AdvisorRegistrationForm form = new AdvisorRegistrationForm();
        form.setToken(token);
        model.addAttribute("registration", form);
        return "yourbusun/confirm-advisor";
    }

    @PostMapping("/confirm")
    public String createAdvisor(@Valid @ModelAttribute("registration") AdvisorRegistrationForm form, BindingResult bindingResult, Model model)
    {
        if (bindingResult.hasErrors())
        {
            return "yourbusun/confirm-advisor";
        }

        // Not always getting checked for some reason?
        if (!form.passwordsMatch())
        {
            model.addAttribute("error", "Passwords do not match");
            return "yourbusun/confirm-advisor";
        }

        Optional<Advisor> newAdvisor = advisorService.createAdvisorFromToken(form.getToken(), form.getPassword(), form.getPhoneNumber());
        if (newAdvisor.isPresent())
        {
            advisorService.authenticateAs(newAdvisor.get());
            return "redirect:/yourbusun/";
        }
        else
        {
            model.addAttribute("error", "Invalid information provided");
            return "yourbusun/confirm-advisor";
        }
    }

    /**
     * JSON request to add advisors.
     */
    @Data
    public static class AddAdvisorsRequest
    {
        private Long schoolId;
        private List<AdvisorRequest> advisors;
    }
}
