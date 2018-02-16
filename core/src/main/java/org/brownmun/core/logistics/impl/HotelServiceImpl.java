package org.brownmun.core.logistics.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.brownmun.core.logistics.HotelService;
import org.brownmun.db.logistics.Hotel;
import org.brownmun.db.logistics.HotelRepository;

@Service
public class HotelServiceImpl implements HotelService
{
    /*
     * There's not much here, but it's nice to have an abstraction over the Spring
     * Data repository interface, and this is a good place for any additional logic
     * that comes up.
     */

    private final HotelRepository hotelRepo;

    @Autowired
    public HotelServiceImpl(HotelRepository hotelRepo)
    {
        this.hotelRepo = hotelRepo;
    }

    @Override
    public List<Hotel> allHotels()
    {
        return hotelRepo.findAll();
    }
}
