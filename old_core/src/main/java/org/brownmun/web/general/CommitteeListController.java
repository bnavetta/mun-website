package org.brownmun.web.general;

import com.google.common.collect.Lists;
import org.brownmun.model.committee.Committee;
import org.brownmun.model.committee.CommitteeType;
import org.brownmun.model.committee.JointCrisis;
import org.brownmun.model.repo.CommitteeRepository;
import org.brownmun.model.repo.JointCrisisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/staff/committees")
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
        List<Committee> generalCommittes = Lists.newArrayList(repo.findAllByCommitteeType(CommitteeType.GENERAL));
        Collections.sort(generalCommittes, Comparator.comparing(Committee::getName));
        model.addAttribute("generalCommittees", generalCommittes);

        List<Committee> specializedCommittees = Lists.newArrayList(repo.findAllByCommitteeType(CommitteeType.SPECIALIZED));
        Collections.sort(specializedCommittees, Comparator.comparing(Committee::getName));
        model.addAttribute("specializedCommittees", specializedCommittees);

        List<JointCrisis> jointCrises = Lists.newArrayList(jointCrisisRepo.findAll());
        Collections.sort(jointCrises, Comparator.comparing(JointCrisis::getName));
        model.addAttribute("jointCrises", jointCrises);

        List<Committee> crisisCommittees = Lists.newArrayList(repo.findNonJointCrisisCommittees());
        Collections.sort(crisisCommittees, Comparator.comparing(Committee::getName));
        model.addAttribute("crisisCommittees", crisisCommittees);
        return "staff/committees";
    }
}
