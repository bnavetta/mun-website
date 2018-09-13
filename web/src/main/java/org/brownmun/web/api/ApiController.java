package org.brownmun.web.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.brownmun.core.committee.CommitteeListing;
import org.brownmun.core.committee.CommitteeService;
import org.brownmun.core.logistics.HotelService;
import org.brownmun.core.logistics.model.Hotel;

/**
 * Read-only API for client-side code on the public facing website, such as the
 * committees list.
 */
@RestController
@RequestMapping("/api")
public class ApiController
{
    private final CommitteeService committees;
    private final HotelService hotels;

    @Autowired
    public ApiController(CommitteeService committees, HotelService hotels)
    {
        this.committees = committees;
        this.hotels = hotels;
    }

    @GetMapping("/committee")
    public CommitteeListing listCommittees()
    {
        return committees.list();
    }

    @GetMapping("/hotels")
    public List<Hotel> getHotels()
    {
        return hotels.allHotels();
    }
}
