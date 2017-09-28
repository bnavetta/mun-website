package org.brownmun.model.advisor;

import com.google.common.base.MoreObjects;
import org.brownmun.model.School;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

/**
 * A school advisor.
 */
@Entity
public class Advisor implements UserDetails
{
    private static final List<GrantedAuthority> ADVISOR_ROLES = AuthorityUtils.createAuthorityList("ROLE_ADVISOR");
    private static final List<GrantedAuthority> PRIMARY_ADVISOR_ROLES = AuthorityUtils.createAuthorityList("ROLE_ADVISOR", "ROLE_PRIMARY_ADVISOR");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Is this advisor the school's primary advisor (the one who registered)?
     */
    private boolean isPrimary;

    private String name;

    private String email;

    private String phoneNumber;

    private String password;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    public Long getId()
    {
        return id;
    }

    public boolean isPrimary()
    {
        return isPrimary;
    }

    public void setPrimary(boolean primary)
    {
        isPrimary = primary;
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

    @Override
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
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return isPrimary ? PRIMARY_ADVISOR_ROLES : ADVISOR_ROLES;
    }

    @Override
    public String getUsername()
    {
        return email;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("isPrimary", isPrimary)
                .add("name", name)
                .add("email", email)
                .add("phoneNumber", phoneNumber)
                .add("password", password)
                .add("school_id", school.getId())
                .toString();
    }
}
