package org.brownmun.web.security;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.Http401AuthenticationEntryPoint;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;

@Configuration
//@EnableWebSecurity
@EnableOAuth2Client
@EnableConfigurationProperties(StaffProperties.class)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private OAuth2ClientContext oauth2ClientContext;

    @Autowired
    private AdvisorService advisorService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private HttpServletRequest requestProxy;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StaffPrincipalExtractor principalExtractor;

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        if (!Strings.isNullOrEmpty(securityProperties.getHeaders().getContentSecurityPolicy()))
        {
            http.headers().contentSecurityPolicy(securityProperties.getHeaders().getContentSecurityPolicy());
        }

        http
            .csrf()
                .ignoringAntMatchers("/admin/print/sock/**")
                .and()
            .headers()
                .referrerPolicy(ReferrerPolicy.NO_REFERRER).and()
                .and()
            .authorizeRequests()
                .antMatchers("/yourmun/add-advisors/confirm").permitAll()
                .antMatchers("/advisor/forgot-password").permitAll()
                .antMatchers("/advisor/reset-password").permitAll()
                .antMatchers("/yourmun/**").hasRole("ADVISOR")
                .antMatchers("/advisor/**").hasRole("ADVISOR")
                .antMatchers("/admin/**").hasRole("STAFF")
                .antMatchers("/register").hasRole("STAFF") // probably going away
                .and()
            .exceptionHandling()
                .accessDeniedHandler(new StaffAwareAccessDeniedHandler())
                .and()
            .formLogin()
                .loginPage("/login")
                .successHandler(new SavedRequestAwareAuthenticationSuccessHandler())
                .defaultSuccessUrl("/yourmun/")
                .permitAll()
                .and()
            .logout()
                .deleteCookies("JSESSIONID")
                .permitAll()
                .and()
            .rememberMe()
                .userDetailsService(advisorService)
                .key("remember-me")
                .alwaysRemember(true)
            .and()
                .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class)
            .exceptionHandling()
                .defaultAuthenticationEntryPointFor(new LoginUrlAuthenticationEntryPoint("/login/staff"), new AntPathRequestMatcher("/admin/**"))
                .defaultAuthenticationEntryPointFor(new Http401AuthenticationEntryPoint("Bearer realm=\"busun.org\""), new AntPathRequestMatcher("/api/**"));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(advisorService).passwordEncoder(passwordEncoder);
    }

    private Filter ssoFilter()
    {
        OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter("/login/staff");
        OAuth2RestTemplate template = new OAuth2RestTemplate(google(), oauth2ClientContext);
        filter.setRestTemplate(template);
        UserInfoTokenServices tokenServices = new UserInfoTokenServices(googleResource().getUserInfoUri(), google().getClientId());
        tokenServices.setRestTemplate(template);
        tokenServices.setAuthoritiesExtractor(authoritiesExtractor());
        tokenServices.setPrincipalExtractor(principalExtractor);
        filter.setTokenServices(tokenServices);
        filter.setAuthenticationSuccessHandler(new SavedRequestAwareAuthenticationSuccessHandler());
        return filter;
    }

    @Bean
    @ConfigurationProperties("google.client")
    public AuthorizationCodeResourceDetails google()
    {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("google.resource")
    public ResourceServerProperties googleResource()
    {
        return new ResourceServerProperties();
    }

    @Bean
    public AuthoritiesExtractor authoritiesExtractor()
    {
        return new DomainRestrictedAuthoritiesExtractor(staffService, requestProxy);
    }

    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter)
    {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }
}
