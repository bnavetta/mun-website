package org.brownmun.web.general;

import org.brownmun.model.CommitteeType;
import org.brownmun.model.repo.CommitteeRepository;
import org.brownmun.model.repo.JointCrisisRepository;
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
    private final JointCrisisRepository jointCrisisRepo;

    @Autowired
    public CommitteeListController(CommitteeRepository repo, JointCrisisRepository jointCrisisRepo)
    {
        this.repo = repo;
        this.jointCrisisRepo = jointCrisisRepo;
    }

    @GetMapping
    public String committees(Model model)
    {
        model.addAttribute("generalCommittees", repo.findAllByCommitteeType(CommitteeType.GENERAL));
        model.addAttribute("specializedCommittees", repo.findAllByCommitteeType(CommitteeType.SPECIALIZED));
        model.addAttribute("jointCrises", jointCrisisRepo.findAll());
        model.addAttribute("crisisCommittees", repo.findNonJointCrisisCommittees());
        return "conference/committees";
    }
}
