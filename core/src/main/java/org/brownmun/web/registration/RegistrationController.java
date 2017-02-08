package org.brownmun.web.registration;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import lombok.Data;
import org.brownmun.model.RegistrationStatus;
import org.brownmun.model.School;
import org.brownmun.model.SchoolLogistics;
import org.brownmun.model.repo.HotelRepository;
import org.brownmun.model.repo.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Locale;
import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegistrationController
{
	// For resolving error messages
	private final MessageSource messageSource;

	private final HotelRepository hotelRepo;
	private final SchoolRepository schoolRepo;

	@Autowired
	public RegistrationController(MessageSource messageSource, HotelRepository hotelRepo, SchoolRepository schoolRepo)
	{
		this.messageSource = messageSource;
		this.hotelRepo = hotelRepo;
		this.schoolRepo = schoolRepo;
	}

	@GetMapping
	public String registrationForm(Model model)
	{
		model.addAttribute("form", new RegistrationForm());
		return "registration/register";
	}

	@PostMapping(produces = "application/json")
	@ResponseBody
	public ResponseEntity<RegistrationResult> saveRegistration(@Valid @RequestBody RegistrationForm form, BindingResult bindingResult, Locale locale)
	{
		if (bindingResult.hasErrors())
		{
			Multimap<String, String> errors = bindingResult.getFieldErrors().stream().collect(Multimaps.toMultimap(
				e -> e.getField(),
				e -> messageSource.getMessage(e.getCode(), e.getArguments(), e.getDefaultMessage(), locale),
				ArrayListMultimap::create
			));

			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new RegistrationResult(false, errors, null));
		}

		School school = new School();
		school.setName(form.getSchoolName());
		school.setStatus(RegistrationStatus.REGISTERED);
		school.setAddress(form.getSchoolAddress());
		school.setPhoneNumber(form.getSchoolPhoneNumber());
		school.setNumberOfYearsAttended(form.getNumberOfYearsAttended());
		school.setYearsAttended(form.getYearsAttended());

		SchoolLogistics logistics = new SchoolLogistics();
		logistics.setUsingShuttle(form.isBusunShuttles());
		if (form.isBusunHotel())
		{
			logistics.setHotel(hotelRepo.findOne(form.getHotelSelection()));
		}
		school.setLogistics(logistics);

		school.setAboutText(form.getAboutSchool());
		school.setRequestedDelegates(form.getEstimatedDelegates());
		school.setRequestedChaperones(form.getEstimatedChaperones());
		school.setRegistrationTime(Instant.now());

		School saved = schoolRepo.save(school);

		return ResponseEntity.ok(new RegistrationResult(true, ArrayListMultimap.create(), saved.getId()));
	}

	@Data
	public class RegistrationResult
	{
		final boolean success;
		final Multimap<String, String> errors;
		final Long schoolId;
	}
}
