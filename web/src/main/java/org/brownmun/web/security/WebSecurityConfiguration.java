package org.brownmun.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration
{
    @Configuration
    @Order(2)
    public static class AdvisorConfiguration extends WebSecurityConfigurerAdapter
    {
        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private AdvisorUserDetailsService userDetailsService;

        @Override
        protected void configure(HttpSecurity http) throws Exception
        {
            http.antMatcher("/your-mun/**")
                    .csrf().disable()
                    .formLogin()
                    .loginPage("/your-mun/login")
                    .loginProcessingUrl("/your-mun/login")
                    .permitAll()
                    .and()
                    .authorizeRequests().anyRequest().hasRole("ADVISOR");
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception
        {
            auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        }
    }

    @Configuration
    @Order(3)
    public static class PublicConfiguration extends WebSecurityConfigurerAdapter
    {
        @Override
        protected void configure(HttpSecurity http) throws Exception
        {
            http
                    .cors().and()
                    .csrf().disable()
                    .authorizeRequests().antMatchers("/**").permitAll();
        }
    }
}
