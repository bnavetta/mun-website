package org.brownmun.web.security;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import org.brownmun.core.school.model.Advisor;

/**
 * Represents a staff member.
 *
 * A note on the access policy re: {@link #canAccessCommittee(long)},
 * {@link #canAccessSchool(long)}, and so on:
 *
 * The staff can access and modify anything. Other staff members have read-only
 * access to school information. Staff can modify their associated committees.
 */
public class StaffUser implements User, OidcUser
{
    private final String email;
    private final String name;
    private final List<GrantedAuthority> authorities;

    private final Set<Long> committeeIds;
    private final boolean isSecretariat;
    private final OidcUser underlying;

    public StaffUser(OidcUser underlying, Set<Long> committeeIds, boolean isSecretariat)
    {
        this.email = underlying.getEmail();
        this.name = underlying.getName();
        this.committeeIds = committeeIds;
        this.isSecretariat = isSecretariat;
        this.authorities = isSecretariat ? AuthorityUtils.createAuthorityList("ROLE_SECRETARIAT", "ROLE_STAFF")
                : AuthorityUtils.createAuthorityList("ROLE_STAFF");
        this.underlying = underlying;
    }

    @Override
    public String getEmail()
    {
        return email;
    }

    @Override
    public boolean isAdvisor()
    {
        return false;
    }

    @Override
    public boolean isStaff()
    {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes()
    {
        return Map.of("email", email, "name", name, "committeeIds", committeeIds, "isSecretariat", isSecretariat);
    }

    @Override
    public Advisor asAdvisor()
    {
        throw new IllegalStateException("Not an advisor");
    }

    @Override
    public String getPassword()
    {
        return null;
    }

    @Override
    public String getUsername()
    {
        return email;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public boolean canAccessSchool(long schoolId)
    {
        return true;
    }

    @Override
    public boolean canModifySchool(long schoolId)
    {
        return isSecretariat;
    }

    @Override
    public boolean canAccessCommittee(long committeeId)
    {
        return true;
    }

    @Override
    public boolean canModifyCommittee(long committeeId)
    {
        return isSecretariat || committeeIds.contains(committeeId);
    }

    @Override
    public Map<String, Object> getClaims()
    {
        return underlying.getClaims();
    }

    @Override
    public OidcUserInfo getUserInfo()
    {
        return underlying.getUserInfo();
    }

    @Override
    public OidcIdToken getIdToken()
    {
        return underlying.getIdToken();
    }
}
