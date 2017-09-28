package org.brownmun.web.security;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

@ConfigurationProperties("sso")
public class SsoProperties
{
    /**
     * Allowed Google Apps for Work domains. Any user in one of these domains will be allowed.
     */
    private List<String> allowedDomains = Lists.newArrayList();

    /**
     * A whitelist of email addresses to allow, from any domain.
     */
    private List<String> allowedEmails = Lists.newArrayList();

    /**
     * The authorities (roles) to grant to any SSO user.
     */
    private List<String> grantedAuthorities = Lists.newArrayList();

    public List<GrantedAuthority> getAuthorities()
    {
        return getGrantedAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public List<String> getAllowedDomains()
    {
        return allowedDomains;
    }

    public void setAllowedDomains(List<String> allowedDomains)
    {
        this.allowedDomains = allowedDomains;
    }

    public List<String> getAllowedEmails()
    {
        return allowedEmails;
    }

    public void setAllowedEmails(List<String> allowedEmails)
    {
        this.allowedEmails = allowedEmails;
    }

    public List<String> getGrantedAuthorities()
    {
        return grantedAuthorities;
    }

    public void setGrantedAuthorities(List<String> grantedAuthorities)
    {
        this.grantedAuthorities = grantedAuthorities;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("allowedDomains", allowedDomains)
                .add("allowedEmails", allowedEmails)
                .add("grantedAuthorities", grantedAuthorities)
                .toString();
    }
}
