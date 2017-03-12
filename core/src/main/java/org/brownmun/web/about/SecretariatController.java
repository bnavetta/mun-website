package org.brownmun.web.about;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Because we want to feel like real people.
 */
@Controller
@RequestMapping("/about/secretariat")
public class SecretariatController
{
    private final SecProperties sec;

    @Autowired
    public SecretariatController(SecProperties sec)
    {
        this.sec = sec;
    }

    @GetMapping
    public String secretariat(Model model)
    {
        model.addAttribute("sec", sec.getMembers());
        return "about/secretariat";
    }
}
