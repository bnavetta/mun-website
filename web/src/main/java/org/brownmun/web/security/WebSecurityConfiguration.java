package org.brownmun.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import org.brownmun.core.staff.StaffService;

@Configuration
// @EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter
{
    private static final String CONTENT_SECURITY_POLICY =
            "default-src 'self'; frame-src 'none'; img-src 'self' https://storage.googleapis.com; manifest-src 'none'; media-src 'none'; object-src 'none'; script-src 'self' https://maps.googleapis.com https://sentry.io; style-src 'self'; worker-src 'none'; base-uri 'self'; form-action 'self'; frame-ancestors 'none'; report-uri https://brownmun.report-uri.com/r/d/csp/enforce";

    private static final String FEATURE_POLICY =
            "accelerometer 'none'; ambient-light-sensor 'none'; autoplay 'none'; camera 'none'; encrypted-media 'none'; fullscreen 'none'; geolocation 'none'; gyroscope 'none'; magnetometer 'none'; microphone 'none'; midi 'none'; payment 'none'; picture-in-picture 'none'; speaker 'self'; usb 'none'; vibrate 'none'; vr 'none'";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdvisorUserDetailsService userDetailsService;

    @Autowired
    private StaffService staffService;

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
                .csrf().and()
                .cors().and()
                .headers()
                    .referrerPolicy(ReferrerPolicy.NO_REFERRER)
                    .and()
                    .contentSecurityPolicy(CONTENT_SECURITY_POLICY)
                    .and()
                    .addHeaderWriter(new FeaturePolicyWriter(FEATURE_POLICY))
                    .and()
                .authorizeRequests()
                    .antMatchers("/staff/**").hasRole("STAFF")
                    .antMatchers("/your-mun/password/**").permitAll()
                    .antMatchers("/your-mun/**").hasRole("ADVISOR")
                    .antMatchers("/registration/register").denyAll()
                    .and()
                .oauth2Login()
                    .loginPage("/staff/login")
                    .permitAll()
                    .userInfoEndpoint()
                        .oidcUserService(new StaffOAuth2UserService(staffService))
                        .and()
                    .and()
                .formLogin()
                    .loginPage("/your-mun/login")
                    .permitAll()
                    .and()
                .logout()
                    .logoutUrl("/logout")
                    .permitAll()
                    .and()
                .exceptionHandling()
                    .defaultAuthenticationEntryPointFor(new LoginUrlAuthenticationEntryPoint("/staff/login"),
                        new AntPathRequestMatcher("/staff/**"))
                    .defaultAuthenticationEntryPointFor(new LoginUrlAuthenticationEntryPoint("/your-mun/login"),
                        new AntPathRequestMatcher("/your-mun/**"));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}
