package org.brownmun.web.api;

import org.brownmun.core.committee.CommitteeListing;
import org.brownmun.core.committee.CommitteeService;
import org.brownmun.core.school.SchoolService;
import org.brownmun.core.school.model.School;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Read-only API for client-side code on the public facing website, such as the
 * committees list.
 */
@RestController
@RequestMapping("/api")
public class ApiController
{
    private final CommitteeService committees;
    private final SchoolService schools;

    @Autowired
    public ApiController(CommitteeService committees, SchoolService schools)
    {
        this.committees = committees;
        this.schools = schools;
    }

    @GetMapping("/committee")
    public CommitteeListing listCommittees()
    {
        return committees.list();
    }

    @GetMapping("/school")
    @PreAuthorize("hasRole('STAFF')")
    public List<School> listSchools()
    {
        return schools.listSchools();
    }
}
