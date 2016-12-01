package org.brownmun.web.admin;

import com.fasterxml.jackson.annotation.JsonView;
import org.brownmun.model.Delegate;
import org.brownmun.model.RegistrationStatus;
import org.brownmun.model.School;
import org.brownmun.model.repo.DelegateRepository;
import org.brownmun.model.repo.PositionRepository;
import org.brownmun.model.repo.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequestMapping("/admin/school")
public class SchoolController
{
	private final SchoolRepository repo;
	private final DelegateRepository delegateRepo;
	private final PositionRepository positionRepo;

	@Autowired
	public SchoolController(SchoolRepository repo, DelegateRepository delegateRepo, PositionRepository positionRepo)
	{
		this.repo = repo;
		this.delegateRepo = delegateRepo;
		this.positionRepo = positionRepo;
	}

	@GetMapping("/list")
	public String list(Model model)
	{
		model.addAttribute("registeredCount", repo.countByStatus(RegistrationStatus.REGISTERED));
		model.addAttribute("waitlistedCount", repo.countByStatus(RegistrationStatus.WAITLISTED));
		model.addAttribute("acceptedCount", repo.countByStatus(RegistrationStatus.ACCEPTED));
		model.addAttribute("deniedCount", repo.countByStatus(RegistrationStatus.DENIED));
		model.addAttribute("droppedCount", repo.countByStatus(RegistrationStatus.DROPPED));
		model.addAttribute("totalCount", repo.count());

		model.addAttribute("registeredDelegateCount", repo.countRequestedDelegatesByStatus(RegistrationStatus.REGISTERED));
		model.addAttribute("waitlistedDelegateCount", repo.countRequestedDelegatesByStatus(RegistrationStatus.WAITLISTED));
		model.addAttribute("acceptedDelegateCount", repo.countRequestedDelegatesByStatus(RegistrationStatus.ACCEPTED));
		model.addAttribute("deniedDelegateCount", repo.countRequestedDelegatesByStatus(RegistrationStatus.DENIED));
		model.addAttribute("droppedDelegateCount", repo.countRequestedDelegatesByStatus(RegistrationStatus.DROPPED));
		model.addAttribute("totalDelegateCount", repo.countRequestedDelegates());

		return "admin/school/list";
	}

	@GetMapping(path = "/list.json", produces = "application/json")
	@JsonView(School.View.Summary.class)
	@ResponseBody
	public Iterable<School> listApi()
	{
		return repo.findAll();
	}

	@PostMapping("/set-status")
	public String setStatus(UriComponentsBuilder builder, @RequestParam("id") Long id, @RequestParam("registrationStatus") RegistrationStatus status)
	{
		School school = repo.findOne(id);
		if (school == null)
		{
			throw new NoSuchSchoolException(id);
		}

		school.setStatus(status);
		repo.save(school);
		return "redirect:" + MvcUriComponentsBuilder.fromMappingName(builder, "SC#profile").arg(0, id).build();
	}

	@GetMapping("/profile/{id}")
	public String profile(@PathVariable("id") Long id, Model model)
	{
		School school = repo.findOne(id);
		if (school == null)
		{
			throw new NoSuchSchoolException(id);
		}

		model.addAttribute("school", school);
		return "admin/school/profile";
	}

	@GetMapping("/profile/{id}/delegates")
	public String delegates(@PathVariable("id") Long id, Model model)
	{
		School school = repo.findOne(id);
		if (school == null)
		{
			throw new NoSuchSchoolException(id);
		}

		model.addAttribute("school", school);
		return "admin/school/delegates";
	}

	// Doing this as a whole-page submit isn't ideal,
	// but making an ajax request and selectively updating the page also isn't ideal...
	// but neither is doing everything in React :(
	@PostMapping("/unassign-delegate")
	public String unassignDelegate(@RequestParam("schoolId") long schoolId, @RequestParam("delegateId") long delegateId, UriComponentsBuilder builder)
	{
		School school = repo.findOne(schoolId);
		if (school == null)
		{
			throw new NoSuchSchoolException(schoolId);
		}

		Delegate delegate = delegateRepo.findOne(delegateId);
		if (delegate == null)
		{
			throw new InvalidDelegateException(delegateId);
		}

		if (!delegate.getSchool().getId().equals(schoolId))
		{
			throw new InvalidDelegateException(delegateId);
		}

		delegate.getPosition().setDelegate(null);
		positionRepo.save(delegate.getPosition());
		return "redirect:" + MvcUriComponentsBuilder.fromMappingName(builder, "SC#delegates").arg(0, schoolId).build();
	}

	@GetMapping("")
	public String index(UriComponentsBuilder builder)
	{
		return "redirect:" + MvcUriComponentsBuilder.fromMappingName(builder, "SC#list").build();
	}
}
