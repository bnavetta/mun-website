package org.brownmun.web.logistics;

import org.brownmun.model.repo.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Public info about hotels.
 */
@Controller
@RequestMapping("/logistics/hotels")
public class HotelController
{
    private final HotelRepository repo;

    @Autowired
    public HotelController(HotelRepository repo)
    {
        this.repo = repo;
    }

    @GetMapping
    public String listHotels(Model model)
    {
        model.addAttribute("hotels", repo.findAll());
        return "logistics/hotels/list";
    }
}
