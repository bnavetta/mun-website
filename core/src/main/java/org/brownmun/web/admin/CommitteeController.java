package org.brownmun.web.admin;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.brownmun.model.Committee;
import org.brownmun.model.CommitteeType;
import org.brownmun.model.Delegate;
import org.brownmun.model.Position;
import org.brownmun.model.repo.CommitteeRepository;
import org.brownmun.model.repo.DelegateRepository;
import org.brownmun.model.repo.PositionRepository;
import org.brownmun.model.repo.SchoolRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/admin/committee")
public class CommitteeController
{
	private final CommitteeRepository repo;
	private final SchoolRepository schoolRepo;
	private final DelegateRepository delegateRepo;
	private final PositionRepository positionRepo;

	@Autowired
	public CommitteeController(CommitteeRepository repo, SchoolRepository schoolRepo, DelegateRepository delegateRepo, PositionRepository positionRepo)
	{
		this.repo = repo;
		this.schoolRepo = schoolRepo;
		this.delegateRepo = delegateRepo;
		this.positionRepo = positionRepo;
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

	@GetMapping("/{id}/positions")
	@Transactional
	public String positions(@PathVariable("id") Long id, Model model)
	{
		Committee committee = repo.findOne(id);
		if (committee == null)
		{
			throw new NoSuchCommitteeException(id);
		}

		model.addAttribute("committee", committee);
		model.addAttribute("schools", schoolRepo.findAll());
		return "admin/committee/positions";
	}

	@PostMapping("/{id}/positions")
	public String savePositions(@PathVariable("id") Long id, @ModelAttribute PositionForm positionForm)
	{
		Committee committee = repo.findOne(id);
		if (committee == null)
		{
			throw new NoSuchCommitteeException(id);
		}

		List<Long> positions = positionForm.positions;
		List<Position> changedPositions = Lists.newArrayList();

		// Can't really do zip in Java :(
		for (int i = 0; i < positions.size(); i++)
		{
			Long newSchoolId = positions.get(i);
			Position position = committee.getPositions().get(i);

			if (newSchoolId == null)
			{
				// assigned -> unassigned
				if (position.isAssigned())
				{
					position.setDelegate(null);
					changedPositions.add(position);
				}
			}
			else
			{
				// unassigned or assigned to different school -> assigned
				if (!position.isAssigned() || !position.getDelegate().getSchool().getId().equals(newSchoolId))
				{
					Optional<Delegate> newDelegate = delegateRepo.findFreeDelegate(newSchoolId);
					if (newDelegate.isPresent())
					{
						position.setDelegate(newDelegate.get());
						changedPositions.add(position);
					}
					else
					{
						throw new NoFreeDelegatesException(newSchoolId);
					}
				}
			}
		}

		positionRepo.save(changedPositions);

		// Redirect so that the changes are flushed to the database and derived fields are recalculated
		return "redirect:" + MvcUriComponentsBuilder.fromMappingName("CC#positions").arg(0, id).build();
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
	public String saveCommittee(@Valid @ModelAttribute Committee committee, BindingResult bindingResult, UriComponentsBuilder builder)
	{
		if (bindingResult.hasErrors()) {
			return "admin/committee/edit";
		}

		log.info("Saving {}", committee);
		Committee saved = repo.save(committee);
		return "redirect:" + MvcUriComponentsBuilder.fromMappingName(builder, "CC#viewCommittee").arg(0, saved.getId()).build();
	}

	@GetMapping("")
	public String index(UriComponentsBuilder builder)
	{
		return "redirect:" + MvcUriComponentsBuilder.fromMappingName(builder, "CC#listCommittees").build();
	}

	// Needed because type erasure prevents using List<String> as a method parameter directly
	@Data
	static class PositionForm
	{
		List<Long> positions;
	}
}
