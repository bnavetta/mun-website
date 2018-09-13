package org.brownmun.web.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Order(101)
@Configuration
@Profile("prod")
public class ProductionSecurityConfiguration extends WebSecurityConfigurerAdapter
{
    @Value("admin.ip-range")
    private String adminIpRange;

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.requiresChannel()
                .anyRequest()
                .requiresSecure()
                .and()
                .requestMatcher(EndpointRequest.toAnyEndpoint())
                .authorizeRequests()
                .anyRequest()
                .hasIpAddress(adminIpRange);
    }
}
