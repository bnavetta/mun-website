package org.brownmun.web.security;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import org.brownmun.core.staff.StaffService;

@Configuration
// @EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final Logger log = LoggerFactory.getLogger(WebSecurityConfiguration.class);

    @Value("${webSecurity.enableCsp:true}")
    private boolean enableCsp;

    // Need 'self' and https://busun.org to allow access on https://www.busun.org
    private static final String CONTENT_SECURITY_POLICY = "connect-src 'self' https://busun.org; default-src 'none'; font-src 'self' https://busun.org https://fonts.googleapis.com https://fonts.gstatic.com; frame-src 'none'; img-src 'self' data: https://busun.org https://storage.googleapis.com https://maps.googleapis.com https://maps.gstatic.com; manifest-src 'none'; media-src 'none'; object-src 'none'; script-src 'self' https://busun.org https://maps.googleapis.com https://sentry.io; style-src 'self' https://busun.org https://fonts.googleapis.com; worker-src 'none'; base-uri 'self'; form-action 'self'; frame-ancestors 'none'; report-uri https://brownmun.report-uri.com/r/d/csp/reportOnly";
    private static final String NOP_CONTENT_SECURITY_POLICY = "default-src *";

    private static final String FEATURE_POLICY = "accelerometer 'none'; ambient-light-sensor 'none'; autoplay 'none'; camera 'none'; encrypted-media 'none'; fullscreen 'none'; geolocation 'none'; gyroscope 'none'; magnetometer 'none'; microphone 'none'; midi 'none'; payment 'none'; picture-in-picture 'none'; speaker 'self'; usb 'none'; vibrate 'none'; vr 'none'";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdvisorUserDetailsService userDetailsService;

    @Autowired
    private StaffService staffService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (enableCsp) {
            log.debug("Using content security policy");
            http.headers().contentSecurityPolicy(CONTENT_SECURITY_POLICY).reportOnly();
        }

        http.csrf().and().cors().and().headers().referrerPolicy(ReferrerPolicy.NO_REFERRER).and()
                .addHeaderWriter(new FeaturePolicyWriter(FEATURE_POLICY))
                .addHeaderWriter(new ExpectCTHeaderWriter(false, Duration.ofDays(2),
                        "https://brownmun.report-uri.com/r/d/ct/reportOnly"))
                .and().authorizeRequests().antMatchers("/staff/**").hasRole("STAFF")
                .antMatchers("/your-mun/password/**").permitAll().antMatchers("/your-mun/**").hasRole("ADVISOR")
                // .antMatchers("/registration/register")
                // .denyAll()
                .and().oauth2Login().loginPage("/staff/login").permitAll().userInfoEndpoint()
                .oidcUserService(new StaffOAuth2UserService(staffService)).and().and().formLogin()
                .loginPage("/your-mun/login").permitAll().and().logout().logoutUrl("/logout").permitAll().and()
                .exceptionHandling()
                .defaultAuthenticationEntryPointFor(new LoginUrlAuthenticationEntryPoint("/staff/login"),
                        new AntPathRequestMatcher("/staff/**"))
                .defaultAuthenticationEntryPointFor(new LoginUrlAuthenticationEntryPoint("/your-mun/login"),
                        new AntPathRequestMatcher("/your-mun/**"));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}
