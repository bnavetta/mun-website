package org.brownmun.core.school.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.google.common.base.MoreObjects;

/**
 * View of the {@link School} table only containing its application status, for
 * an overview
 */
@Entity
public class SchoolStatus
{
    @Id
    private Long id;

    private String name;

    private boolean hasApplied;
    private boolean accepted;
    private String registrationCode;

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

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("id", id).add("name", name).add("hasApplied", hasApplied)
                .add("accepted", accepted).add("registrationCode", registrationCode).toString();
    }
}
