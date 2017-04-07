package org.brownmun.web.yourbusun;

import lombok.extern.slf4j.Slf4j;
import org.brownmun.ConferenceProperties;
import org.brownmun.model.advisor.Advisor;
import org.brownmun.model.LineItem;
import org.brownmun.model.School;
import org.brownmun.model.repo.LineItemRepository;
import org.brownmun.web.security.AdvisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import javax.validation.Valid;

/**
 * Controller for per-school info pages.
 */
@Slf4j
@Controller
@RequestMapping("/yourbusun")
public class YourBusunController
{
    private final ConferenceProperties conferenceProperties;
    private final AdvisorService advisorService;
    private final LineItemRepository lineItemRepository;

    @Autowired
    public YourBusunController(ConferenceProperties conferenceProperties, AdvisorService advisorService, LineItemRepository lineItemRepository)
    {
        this.conferenceProperties = conferenceProperties;
        this.advisorService = advisorService;
        this.lineItemRepository = lineItemRepository;
    }

    // TODO: make sure this doesn't break if staff try to go to the page
    @GetMapping("/")
    public String info(@AuthenticationPrincipal Advisor advisor, Model model)
    {
        advisor = advisorService.load(advisor);
        School school = advisor.getSchool();
        model.addAttribute("loggedIn", advisor);
        model.addAttribute("school", school);
        model.addAttribute("decisionState", conferenceProperties.isDecisionsPublic() ? "public" : "private");

        if (conferenceProperties.isDecisionsPublic() && (school.isRegistered() || school.isAccepted()))
        {
            Collection<LineItem> charges = lineItemRepository.findBySchoolAndType(school, LineItem.Type.CHARGE);
            model.addAttribute("charges", charges);
            model.addAttribute("totalCharges", charges.stream().mapToDouble(LineItem::getAmount).sum());

            Collection<LineItem> payments = lineItemRepository.findBySchoolAndType(school, LineItem.Type.PAYMENT);
            model.addAttribute("payments", payments);
            model.addAttribute("totalPayments", payments.stream().mapToDouble(LineItem::getAmount).sum());

            Collection<LineItem> aidAwards = lineItemRepository.findBySchoolAndType(school, LineItem.Type.AID_AWARD);
            model.addAttribute("aidAwards", aidAwards);
            model.addAttribute("totalAidAwards", aidAwards.stream().mapToDouble(LineItem::getAmount).sum());

            // They can actually do things!
            return "yourbusun/info";
        }
        else
        {
            return "yourbusun/decisions-private";
        }
    }

    @GetMapping("/change-password")
    public String changePasswordForm(Model model)
    {
        model.addAttribute("form", new ChangePasswordForm());
        return "yourbusun/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@AuthenticationPrincipal Advisor advisor, @Valid @ModelAttribute("form") ChangePasswordForm form, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors())
        {
            return "yourbusun/change-password";
        }

        if (!form.getPassword().equals(form.getPasswordConfirm()))
        {
            bindingResult.rejectValue("passwordConfirm", "password.mismatch", "Passwords must match");
            return "yourbusun/change-password";
        }

        log.debug("Changing password for advisor {}", advisor.getEmail());
        advisorService.saveAndLogin(advisorService.load(advisor), form.getPassword());

        // TODO: notification email or something?
        return "redirect:/yourbusun";
    }

        /*
            FOR ADVISOR CREATION: instead of school code, send an email

          If decisions public:
            - Basics
                - School name
                - registration status + description (see above)
                - registration date
            - School Info
                - school address
                - school phone
            - Logistics
                - delegate + chaperone count
                - hotel info
                - shuttle info
            - Advisors
                - name, phone number, email
                - which one is primary
            - Finances
                - financial aid info
                - charges
                - aid awards
                - payments
                - summary (total charges, aid, invoice amount, paid, outstanding balance)
             - committee preferences
                - % general, spec, crisis
                - countries?
             - supplemental form status/info
                - chaperones
                - hotel (if not busun)
                - arrival time
                - luggage storage days
                - shuttle service times
                - wristband color
                - parlipro training count
                - crisis training count
                - Brown info session count
                - campus tour count
                - # cars
                - days of cars parking
                - # buses
                - days of buses parking
             If decisions private:
                - current status
         */
}
