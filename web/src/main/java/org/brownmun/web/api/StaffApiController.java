package org.brownmun.web.api;

import org.brownmun.core.school.AdvisorService;
import org.brownmun.core.school.SchoolService;
import org.brownmun.core.school.model.Advisor;
import org.brownmun.core.school.model.School;
import org.brownmun.core.school.model.SupplementalInfo;
import org.brownmun.web.security.ConferenceSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * API for the staff dashboard
 */
@RestController
@RequestMapping("/api/staff")
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

    @GetMapping("/school")
    public List<School> listSchools()
    {
        return schools.listSchools();
    }

    @GetMapping("/school/{id}/advisors")
    public List<Advisor> getAdvisors(@PathVariable Long id)
    {
        return schools.getAdvisors(id);
    }

    @GetMapping("/school/{id}/supplemental-info")
    public SupplementalInfo getSupplementalInfo(@PathVariable Long id)
    {
        return schools.getSupplementalInfo(id);
    }

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
