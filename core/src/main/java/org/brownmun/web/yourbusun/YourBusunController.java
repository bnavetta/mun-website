package org.brownmun.web.yourbusun;

import org.brownmun.ConferenceProperties;
import org.brownmun.model.Advisor;
import org.brownmun.model.LineItem;
import org.brownmun.model.School;
import org.brownmun.model.repo.LineItemRepository;
import org.brownmun.model.repo.SchoolRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

/**
 * Controller for per-school info pages.
 */
@Controller
@RequestMapping("/yourbusun")
public class YourBusunController
{
    private final ConferenceProperties conferenceProperties;
    private final SchoolRepository schoolRepository;
    private final LineItemRepository lineItemRepository;

    @Autowired
    public YourBusunController(ConferenceProperties conferenceProperties, SchoolRepository schoolRepository, LineItemRepository lineItemRepository)
    {
        this.conferenceProperties = conferenceProperties;
        this.schoolRepository = schoolRepository;
        this.lineItemRepository = lineItemRepository;
    }

    // TODO: make sure this doesn't break if staff try to go to the page
    @GetMapping("/")
//    @RequestMapping("/")
    public String info(@AuthenticationPrincipal Advisor advisor, Model model)
    {
        School school = schoolRepository.findById(advisor.getSchool().getId());
        model.addAttribute("school", school);
        model.addAttribute("decisionState", conferenceProperties.isDecisionsPublic() ? "public" : "private");

        if (conferenceProperties.isDecisionsPublic() && (school.isRegistered() || school.isAccepted()))
        {
            Hibernate.initialize(school.getAdvisors());

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
