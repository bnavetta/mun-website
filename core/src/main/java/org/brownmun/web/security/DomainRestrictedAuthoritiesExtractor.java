package org.brownmun.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private final HttpServletRequest requestProxy; // Spring will make this a proxy that points to the current request

    public DomainRestrictedAuthoritiesExtractor(StaffService staff, HttpServletRequest requestProxy)
    {
        this.staff = staff;
        this.requestProxy = requestProxy;
    }

    @Override
    public List<GrantedAuthority> extractAuthorities(Map<String, Object> map)
    {
        String email = (String) map.get("email");
        Optional<StaffMember> staffMemberOpt = staff.getByEmail(email);
        if (!staffMemberOpt.isPresent()) {
            // This is all pretty ugly, but trying to work around a quirk where throwing a BadCredentialsException
            // doesn't actually log you out

            HttpSession session = requestProxy.getSession(false);
            if (session != null)
            {
                session.invalidate();
            }
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(null);
            SecurityContextHolder.clearContext();
            throw new BadCredentialsException("Not allowed staff email: " + email);
        }
        StaffMember staffMember = staffMemberOpt.get();

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
