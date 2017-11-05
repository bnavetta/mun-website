package org.brownmun.web.admin;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Lists;
import org.brownmun.model.Delegate;
import org.brownmun.model.committee.Position;
import org.brownmun.model.RegistrationStatus;
import org.brownmun.model.School;
import org.brownmun.model.repo.DelegateRepository;
import org.brownmun.model.repo.PositionRepository;
import org.brownmun.model.repo.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/school")
@PreAuthorize("hasRole('SEC')")
public class SchoolController
{
	private final SchoolRepository repo;
	private final PositionRepository positionRepo;
	private final DelegateRepository delegateRepo;

	@Autowired
	public SchoolController(SchoolRepository repo, PositionRepository positionRepo, DelegateRepository delegateRepo)
	{
		this.repo = repo;
		this.positionRepo = positionRepo;
		this.delegateRepo = delegateRepo;
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
		model.addAttribute("availablePositions", positionRepo.findUnassignedPositions());
		return "admin/school/delegates";
	}

	@PostMapping("/name-delegate")
	public String nameDelegate(@RequestParam("schoolId") Long schoolId, @RequestParam("id") Long delegateId, @RequestParam("name") String delegateName, UriComponentsBuilder builder)
	{
		Delegate delegate = delegateRepo.findOne(delegateId);
		if (delegate == null || delegate.getSchool().getId() != schoolId)
		{
			throw new InvalidDelegateException(delegateId);
		}

		delegate.setName(delegateName);
		delegateRepo.save(delegate);

		return "redirect:" + MvcUriComponentsBuilder.fromMappingName(builder, "SC#delegates").arg(0, schoolId).build();
	}

	@PostMapping("/assign-position")
	public String assignPosition(@RequestParam("schoolId") Long schoolId, @RequestParam("positionId") Long positionId, UriComponentsBuilder builder)
	{
		School school = repo.findOne(schoolId);
		if (school == null)
		{
			throw new NoSuchSchoolException(schoolId);
		}

		Position position = positionRepo.findOne(positionId);

		Optional<Delegate> freeDelegate = delegateRepo.findFreeDelegate(schoolId);
		if (freeDelegate.isPresent())
		{
			position.setDelegate(freeDelegate.get());
			positionRepo.save(position);
		}
		else
		{
			throw new NoFreeDelegatesException(schoolId);
		}

		return "redirect:" + MvcUriComponentsBuilder.fromMappingName(builder, "SC#delegates").arg(0, schoolId).build();
	}

	@GetMapping("")
	public String index(UriComponentsBuilder builder)
	{
		return "redirect:" + MvcUriComponentsBuilder.fromMappingName(builder, "SC#list").build();
	}

	@GetMapping("export")
	public ModelAndView export(HttpServletResponse response)
	{
		response.setHeader("Content-Disposition", "attachment; filename=\"schools.xlsx\"");
		List<School> schools = Lists.newArrayList(repo.findAll());
		return new ModelAndView(new SchoolExportView()).addObject("schools", schools);
	}

	// Would be nicer to have REST DELETE request
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable long id)
	{
		repo.delete(id);
		return "redirect:/admin/school";
	}
}
