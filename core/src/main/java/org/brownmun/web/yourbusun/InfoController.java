package org.brownmun.web.yourbusun;

import org.brownmun.ConferenceProperties;
import org.brownmun.model.Advisor;
import org.brownmun.model.School;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final ConferenceProperties conferenceProperties;

    @Autowired
    public InfoController(ConferenceProperties conferenceProperties)
    {
        this.conferenceProperties = conferenceProperties;
    }

    // TODO: make sure this doesn't break if staff try to go to the page
    @GetMapping("/info")
    public String info(@AuthenticationPrincipal Advisor advisor, Model model)
    {
        School school = advisor.getSchool();

        // TODO: extract into message codes
        String registrationInfo = null;
        String registrationDetail = null;
        if (conferenceProperties.isDecisionsPublic())
        {
            if (school.isWaitlisted())
            {
                registrationInfo = "Your delegation is currently on the waiting list for " + conferenceProperties.getName() + ".";
                registrationDetail = "We'll let you know if the secretariat is able to find space for you.";
            }
            else if(school.isAccepted() || school.isRegistered())
            {
                registrationInfo = "Your school has secured a spot at " + conferenceProperties.getName() + "!";
                registrationDetail = "We look forward to seeing you on campus in November";
            }
            else if (school.isDenied())
            {
                registrationInfo = "Unfortunately, we were unable to find a spot for you at " + conferenceProperties.getName() + ".";
                registrationDetail = "Each year, many more schools register than we have space to host. We regret that we cannot offer you a place at "
                    + conferenceProperties.getName()
                    + " this year. However, we encourage you to register early in the spring of next year.";
            }
            else if (school.isDropped())
            {
                registrationInfo = "You received a spot at " + conferenceProperties.getName() + ", but elected to drop out of the conference.";
            }
        }
        else
        {
            if (school.isRegistered() || school.isAccepted())
            {
                registrationInfo = "Your delegation is registered for " + conferenceProperties.getName() + ".";
                registrationDetail = "We'll let you know when the secretariat has reviewed your application.";
            }
            else if (school.isWaitlisted() || school.isDenied())
            {
                registrationInfo = "Your delegation is currently on the waiting list for " + conferenceProperties.getName() + ".";
                registrationDetail = "We'll let you know if the secretariat is able to find space for you.";
            }
        }

        model.addAttribute("decisionsPublic", conferenceProperties.isDecisionsPublic());
        model.addAttribute("registrationInfo", registrationInfo);
        model.addAttribute("registrationDetail", registrationDetail);
        model.addAttribute("school", school);
        return "yourbusun/info";
    }
}
