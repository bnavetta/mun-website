package org.brownmun.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.brownmun.core.school.SchoolService;

@Service
public class AdvisorUserDetailsService implements UserDetailsService
{
    private final SchoolService schoolService;

    @Autowired
    public AdvisorUserDetailsService(SchoolService schoolService)
    {
        this.schoolService = schoolService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException
    {
        return schoolService.findAdvisor(s).map(AdvisorUser::new).orElseThrow(
                () -> new UsernameNotFoundException("No advisor found with email address " + s));
    }
}
