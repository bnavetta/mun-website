package org.brownmun.core.school.model;

import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import org.brownmun.util.PhoneNumber;

/**
 * A school advisor. Usually, this is a teacher.
 */
@Entity
public class Advisor
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The advisor's name
     */
    @NotBlank
    private String name;

    /**
     * The advisor's email address
     */
    @Email
    @NotBlank
    private String email;

    /**
     * The advisor's phone number
     */
    @PhoneNumber
    private String phoneNumber;

    /**
     * A hash of the advisor's password
     */
    @JsonIgnore
    @NotBlank
    private String password;

    /**
     * The school the advisor represents
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    public Long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public School getSchool()
    {
        return school;
    }

    public void setSchool(School school)
    {
        this.school = school;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Advisor advisor = (Advisor) o;
        return Objects.equals(email, advisor.email);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(email);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("id", id).add("name", name).add("email", email)
                .add("phoneNumber", phoneNumber).add("password", password).add("school_id", school.getId()).toString();
    }
}