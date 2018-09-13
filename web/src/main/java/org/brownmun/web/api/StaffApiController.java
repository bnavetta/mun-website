package org.brownmun.web.api;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonView;
import org.brownmun.core.api.Views;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.brownmun.core.school.AdvisorService;
import org.brownmun.core.school.SchoolService;
import org.brownmun.core.school.model.Advisor;
import org.brownmun.core.school.model.School;
import org.brownmun.core.school.model.SupplementalInfo;
import org.brownmun.web.security.ConferenceSecurity;

/**
 * API for the staff dashboard
 */
@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('STAFF')")
public class StaffApiController
{
    private final SchoolService schools;
    private final AdvisorService advisors;

    @Autowired
    public StaffApiController(SchoolService schools, AdvisorService advisors)
    {
        this.schools = schools;
        this.advisors = advisors;
    }

    @JsonView(Views.Staff.class)
    @GetMapping("/school")
    public List<School> listSchools()
    {
        return schools.listSchools();
    }

    @JsonView(Views.Staff.class)
    @GetMapping("/school/{id}/advisors")
    public List<Advisor> getAdvisors(@PathVariable Long id)
    {
        return schools.getAdvisors(id);
    }

    @JsonView(Views.Staff.class)
    @GetMapping("/school/{id}/supplemental-info")
    public SupplementalInfo getSupplementalInfo(@PathVariable Long id)
    {
        return schools.getSupplementalInfo(id);
    }

    @JsonView(Views.Staff.class)
    @GetMapping("/advisors")
    public List<Advisor> listAdvisors()
    {
        return schools.listAdvisors();
    }

    @PutMapping("/advisors/authenticate-as")
    public ResponseEntity<Void> authenticateAsAdvisor(@RequestParam Long advisorId)
    {
        Optional<Advisor> advisorOpt = advisors.getAdvisor(advisorId);
        if (advisorOpt.isPresent())
        {
            ConferenceSecurity.authenticateAsAdvisor(advisorOpt.get());
            return ResponseEntity.ok().build();
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }
}
