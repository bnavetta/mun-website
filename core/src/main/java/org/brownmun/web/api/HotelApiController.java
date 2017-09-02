package org.brownmun.web.api;

import org.brownmun.model.Hotel;
import org.brownmun.model.repo.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/hotel")
@RestController
public class HotelApiController
{
    private final HotelRepository repo;

    @Autowired
    public HotelApiController(HotelRepository repo)
    {
        this.repo = repo;
    }

    @GetMapping(produces = "application/json")
    public Iterable<Hotel> getHotels()
    {
        return repo.findAll();
    }
}
