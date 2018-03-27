package org.brownmun.core.school.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * View over the {@link School} table only containing the
 * school's application to the conference
 */
@Entity
public class SchoolApplication
{
    @Id
    private Long id;

    private String name;

    private Boolean hasAttendedBefore;

    private String yearsAttended;

    private String aboutSchool;

    private String aboutMunProgram;

    private String delegationGoals;

    private String whyApplied;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Boolean getHasAttendedBefore()
    {
        return hasAttendedBefore;
    }

    public void setHasAttendedBefore(Boolean hasAttendedBefore)
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
