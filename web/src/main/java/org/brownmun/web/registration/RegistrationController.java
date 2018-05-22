package org.brownmun.web.registration;

import java.net.URI;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.google.common.collect.Iterables;

import org.brownmun.core.school.SchoolService;
import org.brownmun.core.school.model.Advisor;
import org.brownmun.core.school.model.School;
import org.brownmun.web.advisors.DashboardController;
import org.brownmun.web.security.ConferenceSecurity;

@Controller
@RequestMapping("/registration/register")
public class RegistrationController
{
    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);
    private final SchoolService schoolService;

    @Autowired
    public RegistrationController(SchoolService schoolService)
    {
        this.schoolService = schoolService;
    }

    @GetMapping
    public String registrationForm()
    {
        return "registration/register";
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<RegistrationResult> submitApplication(@RequestBody @Valid Application application)
    {
        log.info("Submitting application for school {}", application.getSchoolName());

        School school = schoolService.registerSchool(application.getSchoolName(), application.getAdvisorName(),
                application.getAdvisorEmail(), application.getAdvisorPassword(), application.getAdvisorPhoneNumber());

        school.setId(school.getId());
        school.setName(school.getName());
        school.setHasAttendedBefore(application.isHasAttendedBefore());
        school.setYearsAttended(application.getYearsAttended());
        school.setAboutSchool(application.getAboutSchool());
        school.setAboutMunProgram(application.getAboutMunProgram());
        school.setDelegationGoals(application.getDelegationGoals());
        school.setWhyApplied(application.getWhyApplied());

        schoolService.submitApplication(school);

        Advisor advisor = Iterables.getOnlyElement(school.getAdvisors());
        URI dashboard = MvcUriComponentsBuilder.fromMethodName(DashboardController.class, "dashboardHome").build()
                .toUri();

        ConferenceSecurity.authenticateAsAdvisor(advisor);

        return ResponseEntity.ok(RegistrationResult.create(school.getId(), advisor.getId(), dashboard));
    }
}
