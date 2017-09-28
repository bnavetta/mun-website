package org.brownmun.web.registration;

import org.brownmun.model.delegation.Address;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Registration form model
 */
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

	/*
	 * Finanical Aid Information
	 */

	boolean applyingForFinancialAid;

	int financialAidPercent;

	String financialAidJustification;

	/*
	 * Logistical Information
	 */

	boolean busunHotel;

	boolean busunShuttles;

	Long hotelSelection;

	/*
	 * Conference Preferences
	 */

	@Valid
	CommitteePreferences committeePreferences;

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

	public static class CommitteePreferences
	{
		@Min(0)
		@Max(100)
		int general;

		@Min(0)
		@Max(100)
		int specialized;

		@Min(0)
		@Max(100)
		int crisis;

		@AssertTrue(message = "Committee type preferences must sum to 100%")
		public boolean preferencesValid()
		{
			return general + specialized + crisis == 100;
		}

		public int getGeneral()
		{
			return general;
		}

		public void setGeneral(int general)
		{
			this.general = general;
		}

		public int getSpecialized()
		{
			return specialized;
		}

		public void setSpecialized(int specialized)
		{
			this.specialized = specialized;
		}

		public int getCrisis()
		{
			return crisis;
		}

		public void setCrisis(int crisis)
		{
			this.crisis = crisis;
		}
	}

	public String getAdvisorName()
	{
		return advisorName;
	}

	public void setAdvisorName(String advisorName)
	{
		this.advisorName = advisorName;
	}

	public String getAdvisorEmail()
	{
		return advisorEmail;
	}

	public void setAdvisorEmail(String advisorEmail)
	{
		this.advisorEmail = advisorEmail;
	}

	public String getAdvisorPassword()
	{
		return advisorPassword;
	}

	public void setAdvisorPassword(String advisorPassword)
	{
		this.advisorPassword = advisorPassword;
	}

	public String getAdvisorPasswordConfirm()
	{
		return advisorPasswordConfirm;
	}

	public void setAdvisorPasswordConfirm(String advisorPasswordConfirm)
	{
		this.advisorPasswordConfirm = advisorPasswordConfirm;
	}

	public String getAdvisorPhoneNumber()
	{
		return advisorPhoneNumber;
	}

	public void setAdvisorPhoneNumber(String advisorPhoneNumber)
	{
		this.advisorPhoneNumber = advisorPhoneNumber;
	}

	public String getSchoolName()
	{
		return schoolName;
	}

	public void setSchoolName(String schoolName)
	{
		this.schoolName = schoolName;
	}

	public Address getSchoolAddress()
	{
		return schoolAddress;
	}

	public void setSchoolAddress(Address schoolAddress)
	{
		this.schoolAddress = schoolAddress;
	}

	public String getSchoolPhoneNumber()
	{
		return schoolPhoneNumber;
	}

	public void setSchoolPhoneNumber(String schoolPhoneNumber)
	{
		this.schoolPhoneNumber = schoolPhoneNumber;
	}

	public int getEstimatedDelegates()
	{
		return estimatedDelegates;
	}

	public void setEstimatedDelegates(int estimatedDelegates)
	{
		this.estimatedDelegates = estimatedDelegates;
	}

	public int getEstimatedChaperones()
	{
		return estimatedChaperones;
	}

	public void setEstimatedChaperones(int estimatedChaperones)
	{
		this.estimatedChaperones = estimatedChaperones;
	}

	public int getNumberOfYearsAttended()
	{
		return numberOfYearsAttended;
	}

	public void setNumberOfYearsAttended(int numberOfYearsAttended)
	{
		this.numberOfYearsAttended = numberOfYearsAttended;
	}

	public String getYearsAttended()
	{
		return yearsAttended;
	}

	public void setYearsAttended(String yearsAttended)
	{
		this.yearsAttended = yearsAttended;
	}

	public String getAboutSchool()
	{
		return aboutSchool;
	}

	public void setAboutSchool(String aboutSchool)
	{
		this.aboutSchool = aboutSchool;
	}

	public boolean isApplyingForFinancialAid()
	{
		return applyingForFinancialAid;
	}

	public void setApplyingForFinancialAid(boolean applyingForFinancialAid)
	{
		this.applyingForFinancialAid = applyingForFinancialAid;
	}

	public int getFinancialAidPercent()
	{
		return financialAidPercent;
	}

	public void setFinancialAidPercent(int financialAidPercent)
	{
		this.financialAidPercent = financialAidPercent;
	}

	public String getFinancialAidJustification()
	{
		return financialAidJustification;
	}

	public void setFinancialAidJustification(String financialAidJustification)
	{
		this.financialAidJustification = financialAidJustification;
	}

	public boolean isBusunHotel()
	{
		return busunHotel;
	}

	public void setBusunHotel(boolean busunHotel)
	{
		this.busunHotel = busunHotel;
	}

	public boolean isBusunShuttles()
	{
		return busunShuttles;
	}

	public void setBusunShuttles(boolean busunShuttles)
	{
		this.busunShuttles = busunShuttles;
	}

	public Long getHotelSelection()
	{
		return hotelSelection;
	}

	public void setHotelSelection(Long hotelSelection)
	{
		this.hotelSelection = hotelSelection;
	}

	public CommitteePreferences getCommitteePreferences()
	{
		return committeePreferences;
	}

	public void setCommitteePreferences(CommitteePreferences committeePreferences)
	{
		this.committeePreferences = committeePreferences;
	}

	public String getCountryChoice1()
	{
		return countryChoice1;
	}

	public void setCountryChoice1(String countryChoice1)
	{
		this.countryChoice1 = countryChoice1;
	}

	public String getCountryChoice2()
	{
		return countryChoice2;
	}

	public void setCountryChoice2(String countryChoice2)
	{
		this.countryChoice2 = countryChoice2;
	}

	public String getCountryChoice3()
	{
		return countryChoice3;
	}

	public void setCountryChoice3(String countryChoice3)
	{
		this.countryChoice3 = countryChoice3;
	}

	public String getCountryChoice4()
	{
		return countryChoice4;
	}

	public void setCountryChoice4(String countryChoice4)
	{
		this.countryChoice4 = countryChoice4;
	}

	public String getCountryChoice5()
	{
		return countryChoice5;
	}

	public void setCountryChoice5(String countryChoice5)
	{
		this.countryChoice5 = countryChoice5;
	}
}
