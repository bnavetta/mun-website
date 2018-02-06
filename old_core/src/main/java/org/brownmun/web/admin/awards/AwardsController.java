package org.brownmun.web.admin.awards;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import org.brownmun.model.committee.Award;
import org.brownmun.model.committee.Committee;
import org.brownmun.model.repo.CommitteeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class AwardsController
{
    private final CommitteeRepository committeeRepo;
    private final AwardsService awardsService;

    @Autowired
    public AwardsController(CommitteeRepository committeeRepo, AwardsService awardsService)
    {
        this.committeeRepo = committeeRepo;
        this.awardsService = awardsService;
    }


    @GetMapping(path = "/admin/committee/{id}/awards", name = "AC#awardsIndex")
    public String index(@PathVariable("id") long id, Model model)
    {
        Committee committee = committeeRepo.findOne(id);
        List<Award> awards = Lists.newArrayList(committee.getAwards());
        awards.sort(Comparator.comparing(Award::getAwardType));

        model.addAttribute("committee", committee);
        model.addAttribute("awards", new AwardsForm(committee.getId(), awards));
        return "admin/awards/awards";
    }

    @PostMapping("/admin/committee/{id}/awards")
    public String saveAwards(@PathVariable("id") long id, @ModelAttribute AwardsForm awards)
    {
       awardsService.saveAwards(awards.getAwards());
        return "redirect:" + MvcUriComponentsBuilder.fromMappingName("CC#viewCommittee").arg(0, id).build();
    }

    @GetMapping("/admin/awards-breakdown")
    public String awardsBreakdown(Model model)
    {
        List<AwardsBreakdown.Entry> committees = Lists.newArrayList();
        for (Committee c : committeeRepo.findAllByOrderByName())
        {
            AwardsBreakdown.Entry e = new AwardsBreakdown.Entry();
            e.setCommitteeId(c.getId());
            e.setCommitteeName(c.getName());
            committees.add(e);
        }

        AwardsBreakdown breakdown = new AwardsBreakdown();
        breakdown.setCommittees(committees);

        model.addAttribute("breakdown", breakdown);
        return "admin/awards/awards-breakdown";
    }

    @PostMapping("/admin/awards-breakdown")
    public String submitAwardsBreakdown(@Valid @ModelAttribute AwardsBreakdown breakdown, BindingResult bindingResult, UriComponentsBuilder builder)
    {
        if (bindingResult.hasErrors())
        {
            return "admin/awards/awards-breakdown";
        }

        awardsService.createAwards(breakdown);
        return "redirect:" + MvcUriComponentsBuilder.fromMappingName(builder, "CC#listCommittees").build();
    }

    @GetMapping("/admin/awards-export")
    @ResponseBody
    public ModelAndView exportAwards()
    {
        return new ModelAndView(new AwardsExportView(), "awards", awardsService.getAllAwards());
    }

}
