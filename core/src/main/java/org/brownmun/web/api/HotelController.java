package org.brownmun.web.api;

import org.brownmun.model.Hotel;
import org.brownmun.model.repo.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/api/hotel")
@Controller
public class HotelController
{
	private final HotelRepository repo;

	@Autowired
	public HotelController(HotelRepository repo)
	{
		this.repo = repo;
	}

	@GetMapping(produces = "application/json")
	@ResponseBody
	public Iterable<Hotel> getHotels()
	{
		return repo.findAll();
	}
}
