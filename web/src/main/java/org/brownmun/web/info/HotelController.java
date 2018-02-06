package org.brownmun.web.info;

import org.brownmun.core.logistics.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/logistics/hotels")
public class HotelController
{
    private final HotelService hotels;

    @Autowired
    public HotelController(HotelService hotels)
    {
        this.hotels = hotels;
    }

    @GetMapping
    public ModelAndView listHotels()
    {
        return new ModelAndView("logistics/hotels", "hotels", hotels.allHotels());
    }
}
