package org.brownmun.web.admin;

import com.google.common.collect.Lists;
import org.brownmun.model.Committee;
import org.brownmun.model.CommitteeType;
import org.brownmun.model.repo.CommitteeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping("/admin/committee")
public class CommitteeController
{
	private final CommitteeRepository repo;

	public CommitteeController(CommitteeRepository repo)
	{
		this.repo = repo;
	}

	@RequestMapping("/list")
	public String listCommittees(Model model)
	{
		Map<CommitteeType, List<Committee>> committees = StreamSupport.stream(repo.findAll().spliterator(), false)
			.collect(Collectors.groupingBy(Committee::getCommitteeType, TreeMap::new, Collectors.toList()));

		model.addAttribute("committees", committees);

		return "admin/committee/list";
	}

	@RequestMapping("/{id}")
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
}
