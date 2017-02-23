package org.brownmun.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import javax.persistence.*;

/**
 * A school advisor.
 */
@Data
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
}
