package org.brownmun.web.registration;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegistrationController
{
	@GetMapping
	public String registrationForm(Model model)
	{
		model.addAttribute("form", new RegistrationForm());
		return "registration/register";
	}

	@PostMapping
	public String saveRegistration(@Valid @ModelAttribute RegistrationForm form, BindingResult bindingResult)
	{
		if (bindingResult.hasErrors())
		{
			return "registration/register";
		}

		// TODO: actually register

		return "redirect:/";
	}
}
