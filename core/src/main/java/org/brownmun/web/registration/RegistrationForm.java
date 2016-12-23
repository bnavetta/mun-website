package org.brownmun.web.registration;

import lombok.Data;
import org.brownmun.model.Address;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Registration form model
 */
@Data
public class RegistrationForm
{
	/*
	 * Advisor Information
	 */

	@NotBlank
	String advisorName;

	@Email
	String advisorEmail;

	@NotBlank
	String advisorPassword;

	@NotBlank
	String advisorPasswordConfirm;

	@NotBlank // TODO: validate further
	String advisorPhoneNumber;

	/*
	 * School Information
	 */

	@NotBlank
	String schoolName;

	@Valid
	Address schoolAddress;

	@NotBlank // TODO: validate further
	String schoolPhoneNumber;

	/*
	 * Delegation Information
	 */

	@Min(1)
	@Max(35)
	int estimatedDelegates;

	@Min(1)
	@Max(35)
	int estimatedChaperones;

	@Min(0)
	@Max(20)
	int numberOfYearsAttended;

	String yearsAttended;

	String aboutSchool;

	boolean applyingForFinancialAid;

	/*
	 * Logistical Information
	 */

	boolean busunHotel;

	boolean busunShuttles;

	/*
	 * Conference Preferences
	 */

	@Min(0)
	@Max(100)
	int preferredGeneralPercentage;

	@Min(0)
	@Max(100)
	int preferredSpecializedPercentage;

	@Min(0)
	@Max(100)
	int preferredCrisisPercentage;

	String countryChoice1;
	String countryChoice2;
	String countryChoice3;
	String countryChoice4;
	String countryChoice5;

	/**
	 * Validation constraint that the advisor password entered matches the password confirmation.
	 * @return true if the passwords match
	 */
	@AssertTrue(message = "Password fields must match")
	public boolean passwordsMatch()
	{
		return advisorPassword.equals(advisorPasswordConfirm);
	}

	@AssertTrue(message = "Committee type preferences must sum to 100%")
	public boolean preferencesValid()
	{
		return preferredGeneralPercentage + preferredSpecializedPercentage + preferredCrisisPercentage == 100;
	}
}
