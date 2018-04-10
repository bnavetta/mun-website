package org.brownmun.web.staff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/staff")
public class StaffController
{
    private static final Logger log = LoggerFactory.getLogger(StaffController.class);

    private final ClientRegistrationRepository clients;

    @Autowired
    public StaffController(ClientRegistrationRepository clients)
    {
        this.clients = clients;
    }

    @GetMapping
    public String staffHome(@AuthenticationPrincipal Object currentUser)
    {
        log.info("Current user: {}", currentUser);

        return "staff/home";
    }

    @GetMapping("/login")
    public String loginPage()
    {
        ClientRegistration google = clients.findByRegistrationId("google");
        log.info("Google: oauth2/authorization/{}", google.getRegistrationId());

        return "staff/login";
    }

    /**
     * Support HTML5 pushState URLs
     */
    @RequestMapping("/**")
    public String ui()
    {
        return "forward:/staff/";
    }
}
