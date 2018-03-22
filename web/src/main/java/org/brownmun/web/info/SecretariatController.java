package org.brownmun.web.info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import org.brownmun.core.secretariat.SecretariatProperties;

/**
 * Controller for the {@code /about/secretariat} page. All it really does is
 * render the secretariat page template with the members from
 * {@link SecretariatProperties}.
 */
@Controller
@RequestMapping("/about/secretariat")
public class SecretariatController
{
    private final SecretariatProperties secProperties;

    @Autowired
    public SecretariatController(SecretariatProperties secProperties)
    {
        this.secProperties = secProperties;
    }

    @GetMapping
    public ModelAndView getSecretariat()
    {
        return new ModelAndView("about/secretariat.html", "secretariat", secProperties.getMembers());
    }
}
