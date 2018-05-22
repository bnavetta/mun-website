package org.brownmun.web.registration;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.brownmun.util.PhoneNumber;

/**
 * Representation of the application form.
 */
public class Application
{
    @NotBlank
    private String schoolName;

    @NotBlank
    private String advisorName;

    @NotBlank
    @Email
    private String advisorEmail;

    @NotBlank
    private String advisorPassword;

    @NotBlank
    @PhoneNumber
    private String advisorPhoneNumber;

    private boolean hasAttendedBefore;

    private String yearsAttended;

    @NotBlank
    private String aboutSchool;

    @NotBlank
    private String aboutMunProgram;

    @NotBlank
    private String delegationGoals;

    @NotBlank
    private String whyApplied;

    public String getSchoolName()
    {
        return schoolName;
    }

    public void setSchoolName(String schoolName)
    {
        this.schoolName = schoolName;
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

    public String getAdvisorPhoneNumber()
    {
        return advisorPhoneNumber;
    }

    public void setAdvisorPhoneNumber(String advisorPhoneNumber)
    {
        this.advisorPhoneNumber = advisorPhoneNumber;
    }

    public boolean isHasAttendedBefore()
    {
        return hasAttendedBefore;
    }

    public void setHasAttendedBefore(boolean hasAttendedBefore)
    {
        this.hasAttendedBefore = hasAttendedBefore;
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

    public String getAboutMunProgram()
    {
        return aboutMunProgram;
    }

    public void setAboutMunProgram(String aboutMunProgram)
    {
        this.aboutMunProgram = aboutMunProgram;
    }

    public String getDelegationGoals()
    {
        return delegationGoals;
    }

    public void setDelegationGoals(String delegationGoals)
    {
        this.delegationGoals = delegationGoals;
    }

    public String getWhyApplied()
    {
        return whyApplied;
    }

    public void setWhyApplied(String whyApplied)
    {
        this.whyApplied = whyApplied;
    }
}
