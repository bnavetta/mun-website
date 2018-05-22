package org.brownmun.web.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.brownmun.core.committee.CommitteeListing;
import org.brownmun.core.committee.CommitteeService;
import org.brownmun.core.logistics.HotelService;
import org.brownmun.core.logistics.model.Hotel;
import org.brownmun.core.school.SchoolService;
import org.brownmun.core.school.model.Advisor;
import org.brownmun.core.school.model.School;
import org.brownmun.core.school.model.SupplementalInfo;

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
    private final HotelService hotels;

    @Autowired
    public ApiController(CommitteeService committees, SchoolService schools, HotelService hotels)
    {
        this.committees = committees;
        this.schools = schools;
        this.hotels = hotels;
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

    @GetMapping("/school/{id}/advisors")
    @PreAuthorize("hasRole('STAFF')")
    public List<Advisor> getAdvisors(@PathVariable Long id)
    {
        return schools.getAdvisors(id);
    }

    @GetMapping("/school/{id}/supplemental-info")
    @PreAuthorize("hasRole('STAFF')")
    public SupplementalInfo getSupplementalInfo(@PathVariable Long id)
    {
        return schools.getSupplementalInfo(id);
    }

    @GetMapping("/advisors")
    @PreAuthorize("hasRole('STAFF')")
    public List<Advisor> listAdvisors()
    {
        return schools.listAdvisors();
    }

    @GetMapping("/hotels")
    public List<Hotel> getHotels()
    {
        return hotels.allHotels();
    }
}
