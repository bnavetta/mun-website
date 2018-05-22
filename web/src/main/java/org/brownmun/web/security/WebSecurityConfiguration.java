package org.brownmun.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import org.brownmun.core.staff.StaffService;

@Configuration
// @EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter
{
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdvisorUserDetailsService userDetailsService;

    @Autowired
    private StaffService staffService;

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.csrf().and().cors().and().authorizeRequests().antMatchers("/staff/**").hasRole("STAFF")
                .antMatchers("/your-mun/password/**").permitAll().antMatchers("/your-mun/**").hasRole("ADVISOR")
                .antMatchers("/registration/register").denyAll().and().oauth2Login().loginPage("/staff/login")
                .permitAll().userInfoEndpoint().oidcUserService(new StaffOAuth2UserService(staffService)).and().and()
                .formLogin().loginPage("/your-mun/login").permitAll().and().logout().logoutUrl("/logout").permitAll()
                .and().exceptionHandling()
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
