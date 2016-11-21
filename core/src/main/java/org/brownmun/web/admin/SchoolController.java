package org.brownmun.web.admin;

import org.brownmun.model.RegistrationStatus;
import org.brownmun.model.repo.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequestMapping("/admin/school")
public class SchoolController
{
	private final SchoolRepository repo;

	@Autowired
	public SchoolController(SchoolRepository repo)
	{
		this.repo = repo;
	}

	@GetMapping("/list")
	public String list(Model model)
	{
		model.addAttribute("registeredCount", repo.countByStatus(RegistrationStatus.REGISTERED));
		model.addAttribute("waitlistedCount", repo.countByStatus(RegistrationStatus.WAITLISTED));
		model.addAttribute("acceptedCount", repo.countByStatus(RegistrationStatus.ACCEPTED));
		model.addAttribute("deniedCount", repo.countByStatus(RegistrationStatus.DENIED));
		model.addAttribute("droppedCount", repo.countByStatus(RegistrationStatus.DROPPED));

		return "admin/school/list";
	}

	@GetMapping("")
	public String index(UriComponentsBuilder builder)
	{
		return "redirect:" + MvcUriComponentsBuilder.fromMappingName(builder, "SC#list").build();
	}
}
