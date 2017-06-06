package org.brownmun.web.yourbusun;

import com.google.common.collect.Lists;
import org.brownmun.model.Hotel;
import org.brownmun.model.School;
import org.brownmun.model.advisor.Advisor;
import org.brownmun.model.delegation.SchoolInfo;
import org.brownmun.model.repo.HotelRepository;
import org.brownmun.model.repo.SchoolRepository;
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

import java.util.List;
import javax.validation.Valid;

/**
 * Controller for editing school info.
 */
//@Controller
//@RequestMapping("/yourbusun/info")
public class EditInfoController
{
    private final AdvisorService advisorService;
    private final SchoolRepository schoolRepo;
    private final HotelRepository hotelRepo;

    @Autowired
    public EditInfoController(AdvisorService advisorService, SchoolRepository schoolRepo, HotelRepository hotelRepo)
    {
        this.advisorService = advisorService;
        this.schoolRepo = schoolRepo;
        this.hotelRepo = hotelRepo;
    }

    @ModelAttribute("hotels")
    public List<Hotel> hotels()
    {
        return Lists.newArrayList(hotelRepo.findAll());
    }

    @ModelAttribute
    public SchoolInfo schoolInfo(@AuthenticationPrincipal Advisor advisor)
    {
        advisor = advisorService.load(advisor);
        School school = advisor.getSchool();
        if (school.getInfo() == null)
        {
            SchoolInfo info = new SchoolInfo();
            info.setId(school.getId());
            school.setInfo(info);
            schoolRepo.save(school);
        }

        return school.getInfo();
    }

    @GetMapping
    public String getForm()
    {
        return "yourbusun/edit-info";
    }

    @PostMapping
    public String save(@ModelAttribute @Valid SchoolInfo schoolInfo, BindingResult bindingResult, @AuthenticationPrincipal Advisor advisor)
    {
        if (bindingResult.hasErrors())
        {
            return "yourbusun/edit-info";
        }

        School school = advisorService.load(advisor).getSchool();
        school.setInfo(schoolInfo);
        schoolRepo.save(school);
        return "redirect:/yourbusun";
    }
}
