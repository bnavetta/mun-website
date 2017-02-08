package org.brownmun.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.List;
import java.util.Map;

@Slf4j
public class DomainRestrictedAuthoritiesExtractor implements AuthoritiesExtractor
{
    @Override
    public List<GrantedAuthority> extractAuthorities(Map<String, Object> map)
    {
        log.info("Got user info: {}", map);
        return AuthorityUtils.createAuthorityList("USER", "STAFF");
    }
}
