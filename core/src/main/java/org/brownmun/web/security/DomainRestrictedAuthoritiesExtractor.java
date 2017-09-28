package org.brownmun.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Map;

public class DomainRestrictedAuthoritiesExtractor implements AuthoritiesExtractor
{
    private static final Logger log = LoggerFactory.getLogger(DomainRestrictedAuthoritiesExtractor.class);
    
    private final SsoProperties ssoProperties;

    public DomainRestrictedAuthoritiesExtractor(SsoProperties ssoProperties)
    {
        this.ssoProperties = ssoProperties;
        log.info("Configuring SSO with {}", ssoProperties);
    }

    @Override
    public List<GrantedAuthority> extractAuthorities(Map<String, Object> map)
    {
        String domain = (String) map.get("hd");
        String email = (String) map.get("email");

        boolean emailAllowed = email != null && ssoProperties.getAllowedEmails().contains(email);
        boolean domainAllowed = domain != null && ssoProperties.getAllowedDomains().contains(domain);

        if (domainAllowed || emailAllowed)
        {
            log.debug("Allowing staff SSO for {} from domain {}", email, domain);
            return ssoProperties.getAuthorities();
        }
        else
        {
            log.warn("Denying staff SSO for {} from domain {}", email, domain);
            throw new BadCredentialsException("Not allowed staff email");
        }
    }
}
