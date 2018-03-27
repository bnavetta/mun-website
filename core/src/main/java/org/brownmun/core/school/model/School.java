package org.brownmun.core.school.model;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

import com.google.common.collect.Sets;

/**
 * Core School entity
 */
@Entity
public class School
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Advisor> advisors = Sets.newHashSet();

    /*******************************
     *  Application status fields  *
     *******************************/

    /**
     * Flag indicating that the school has filled out a complete application.
     */
    private boolean hasApplied;

    /**
     * Flag indicating that the school has been accepted to the secretariat.
     */
    private boolean accepted;

    /**
     * Unique code used to represent this school. Used to register new advisors.
     */
    private String registrationCode;

    /************************
     *  Application fields  *
     ************************/

    /**
     * Have they attended this conference before?
     */
    private Boolean hasAttendedBefore;

    /**
     * In which years (free-form) did they attend?
     */
    private String yearsAttended;

    /**
     * Describes the school
     */
    private String aboutSchool;

    /**
     * Describes the school's Model UN program
     */
    private String aboutMunProgram;

    /**
     * The delegation's goals for the conference
     */
    private String delegationGoals;

    /**
     * Why the school applied
     */
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

    public boolean isHasApplied()
    {
        return hasApplied;
    }

    public void setHasApplied(boolean hasApplied)
    {
        this.hasApplied = hasApplied;
    }

    public boolean isAccepted()
    {
        return accepted;
    }

    public void setAccepted(boolean accepted)
    {
        this.accepted = accepted;
    }

    public String getRegistrationCode()
    {
        return registrationCode;
    }

    public void setRegistrationCode(String registrationCode)
    {
        this.registrationCode = registrationCode;
    }

    public Set<Advisor> getAdvisors()
    {
        return Collections.unmodifiableSet(advisors);
    }

    public void addAdvisor(Advisor advisor)
    {
        advisors.add(advisor);
        advisor.setSchool(this);
    }

    public void removeAdvisor(Advisor advisor)
    {
        advisors.remove(advisor);
        advisor.setSchool(null);
    }

    public void setAdvisors(Set<Advisor> advisors)
    {
        this.advisors = advisors;
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        School school = (School) o;
        return Objects.equals(name, school.name);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name);
    }
}
