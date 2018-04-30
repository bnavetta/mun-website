package org.brownmun.web.security;

import org.brownmun.core.school.model.Advisor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;
import java.util.List;

/**
 * A {@link User} backed by an {@link Advisor}.
 */
public class AdvisorUser implements User
{
    private static final List<GrantedAuthority> AUTHORITIES = AuthorityUtils.createAuthorityList("ROLE_ADVISOR");

    private final Advisor advisor;

    public AdvisorUser(Advisor advisor)
    {
        this.advisor = advisor;
    }

    @Override
    public boolean isAdvisor()
    {
        return true;
    }

    @Override
    public boolean isStaff()
    {
        return false;
    }

    @Override
    public Advisor asAdvisor()
    {
        return advisor;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return AUTHORITIES;
    }

    @Override
    public String getPassword()
    {
        return advisor.getPassword();
    }

    @Override
    public String getUsername()
    {
        return advisor.getEmail();
    }

    @Override
    public String getName()
    {
        return advisor.getName();
    }

    @Override
    public String getEmail()
    {
        return advisor.getEmail();
    }

    @Override
    public boolean canAccessSchool(long schoolId)
    {
        return advisor.getSchool().getId() == schoolId;
    }

    @Override
    public boolean canModifySchool(long schoolId)
    {
        return advisor.getSchool().getId() == schoolId;
    }

    @Override
    public boolean canAccessCommittee(long committeeId)
    {
        return false;
    }

    @Override
    public boolean canModifyCommittee(long committeeId)
    {
        return false;
    }
}
