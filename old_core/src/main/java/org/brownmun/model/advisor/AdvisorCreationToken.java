package org.brownmun.model.advisor;

import com.google.common.base.MoreObjects;
import org.brownmun.model.School;

import javax.persistence.*;
import java.util.Objects;

/**
 * Token for adding a new school advisor
 */
@Entity
public class AdvisorCreationToken
{
    /**
     * Randomly-generated token sent in the creation email
     */
    @Id
    private String token;

    /**
     * The school the advisor is part of
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    /**
     * Name of advisor being created
     */
    private String advisorName;

    /**
     * Email address of advisor being created
     */
    private String advisorEmail;

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public School getSchool()
    {
        return school;
    }

    public void setSchool(School school)
    {
        this.school = school;
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdvisorCreationToken that = (AdvisorCreationToken) o;
        return Objects.equals(token, that.token) &&
                Objects.equals(advisorName, that.advisorName) &&
                Objects.equals(advisorEmail, that.advisorEmail);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(token, advisorName, advisorEmail);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("token", token)
                .add("school_id", school.getId())
                .add("advisorName", advisorName)
                .add("advisorEmail", advisorEmail)
                .toString();
    }
}
