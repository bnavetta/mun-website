package org.brownmun.web.admin;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.brownmun.model.Committee;
import org.brownmun.model.CommitteeType;
import org.brownmun.model.Position;
import org.brownmun.model.repo.CommitteeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Controller
@RequestMapping("/admin/committee")
public class CommitteeController
{
	private final CommitteeRepository repo;

	@Autowired
	public CommitteeController(CommitteeRepository repo)
	{
		this.repo = repo;
	}

	@GetMapping("/list")
	public String listCommittees(Model model)
	{
		Map<CommitteeType, List<Committee>> committees = StreamSupport.stream(repo.findAll().spliterator(), false)
			.collect(Collectors.groupingBy(Committee::getCommitteeType, TreeMap::new, Collectors.toList()));

		model.addAttribute("committees", committees);

		return "admin/committee/list";
	}

	@GetMapping("/{id}")
	public String viewCommittee(@PathVariable("id") Long id, Model model)
	{
		Committee committee = repo.findOne(id);
		if (committee == null)
		{
			throw new NoSuchCommitteeException(id);
		}

		model.addAttribute("committee", committee);
		return "admin/committee/view";
	}

	@GetMapping("/{id}/edit")
	public String editCommittee(@PathVariable("id") Long id, Model model)
	{
		Committee committee = repo.findOne(id);
		if (committee == null)
		{
			throw new NoSuchCommitteeException(id);
		}

		model.addAttribute("committee", committee);
		model.addAttribute("creating", false);
		return "admin/committee/edit";
	}

	@GetMapping("/{id}/add-positions")
	public String addPositions(@PathVariable("id") Long id, Model model)
	{
		Committee committee = repo.findOne(id);
		if (committee == null)
		{
			throw new NoSuchCommitteeException(id);
		}

		model.addAttribute("committeeName", committee.getName());
		model.addAttribute("committeeId", id);
		return "admin/committee/add-positions";
	}

	@PostMapping("/{id}/add-positions")
	@ResponseBody
	public Long saveAddedPositions(@PathVariable("id") Long id, @RequestBody Set<String> positions)
	{
		Committee committee = repo.findOne(id);
		if (committee == null)
		{
			throw new NoSuchCommitteeException(id);
		}

		committee.getPositions().addAll(positions.stream().map(name -> {
			Position posn = new Position();
			posn.setCommittee(committee);
			posn.setName(name);
			return posn;
		}).collect(Collectors.toList()));
		return repo.save(committee).getId();
	}

	@GetMapping("/create")
	public String newCommittee(Model model)
	{
		model.addAttribute("committee", new Committee());
		model.addAttribute("creating", true);
		return "admin/committee/edit";
	}

	@PostMapping("/save")
	public String saveCommittee(@ModelAttribute Committee committee, UriComponentsBuilder builder)
	{
		log.info("Saving {}", committee);
		Committee saved = repo.save(committee);
		return "redirect:" + MvcUriComponentsBuilder.fromMappingName(builder, "CC#viewCommittee").arg(0, saved.getId()).build();
	}

	@GetMapping("")
	public String index(UriComponentsBuilder builder)
	{
		return "redirect:" + MvcUriComponentsBuilder.fromMappingName(builder, "CC#listCommittees").build();
	}
}
