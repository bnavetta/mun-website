package org.brownmun.web.admin;

import org.brownmun.model.committee.Position;
import org.brownmun.model.repo.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/position")
public class PositionController
{
	private final PositionRepository repo;

	@Autowired
	public PositionController(PositionRepository repo)
	{
		this.repo = repo;
	}

	@PostMapping("/unassign")
	public String unassign(@RequestParam("id") Long positionId, @RequestParam("redirect") String redirectUrl)
	{
		Position position = repo.findOne(positionId);
		// TODO: handle not found
		position.setDelegate(null);
		repo.save(position);
		return "redirect:" + redirectUrl;
	}
}
