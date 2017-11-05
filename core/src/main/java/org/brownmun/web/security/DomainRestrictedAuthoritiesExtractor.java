package org.brownmun.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.List;
import java.util.Map;

public class DomainRestrictedAuthoritiesExtractor implements AuthoritiesExtractor
{
    private static final Logger log = LoggerFactory.getLogger(DomainRestrictedAuthoritiesExtractor.class);

    private static final List<GrantedAuthority> SEC_AUTHORITIES =
            AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_STAFF", "ROLE_ACTUATOR", "ROLE_SEC");
    private static final List<GrantedAuthority> OPS_AUTHORITIES =
            AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_STAFF", "ROLE_OPS");
    private static final List<GrantedAuthority> COMMITTEE_AUTHORITIES =
            AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_STAFF", "ROLE_COMMITTEE");

    private final StaffService staff;

    public DomainRestrictedAuthoritiesExtractor(StaffService staff)
    {
        this.staff = staff;
    }

    @Override
    public List<GrantedAuthority> extractAuthorities(Map<String, Object> map)
    {
        String email = (String) map.get("email");
        StaffMember staffMember = staff.getByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Not allowed staff email: " + email));

        switch (staffMember.getType())
        {
            case SECRETARIAT:
                return SEC_AUTHORITIES;
            case OPS:
                return OPS_AUTHORITIES;
            case COMMITTEE:
                return COMMITTEE_AUTHORITIES;
            default:
                throw new IllegalStateException("Unknown staff type: " + staffMember.getType());
        }
    }
}
