package org.brownmun.web.general;

import org.brownmun.model.CommitteeType;
import org.brownmun.model.repo.CommitteeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/conference/committees")
public class CommitteeListController
{
    private final CommitteeRepository repo;

    @Autowired
    public CommitteeListController(CommitteeRepository repo)
    {
        this.repo = repo;
    }

    @GetMapping
    public String committees(Model model)
    {
        model.addAttribute("generalCommittees", repo.findAllByCommitteeType(CommitteeType.GENERAL));
        model.addAttribute("specializedCommittees", repo.findAllByCommitteeType(CommitteeType.SPECIALIZED));
        // TODO: crisis
        return "conference/committees";
    }
}
