package org.brownmun.db.school;

import com.google.common.collect.Sets;

import javax.persistence.*;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

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

    /**
     * Flag indicating that the school has filled out a complete application.
     */
    private boolean hasApplied;

    /**
     * Flag indicating that the school has been accepted to the conference.
     */
    private boolean accepted;

    /**
     * Unique code used to represent this school. Used to register new advisors.
     */
    private String registrationCode;

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Advisor> advisors = Sets.newHashSet();

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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        School school = (School) o;
        return Objects.equals(name, school.name);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name);
    }
}
