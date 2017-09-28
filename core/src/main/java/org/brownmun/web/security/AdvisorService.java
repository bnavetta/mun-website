package org.brownmun.web.security;

import org.brownmun.model.advisor.Advisor;
import org.brownmun.model.repo.AdvisorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service
public class AdvisorService implements UserDetailsService
{
    private static final Logger log = LoggerFactory.getLogger(AdvisorService.class);
    
    private final AdvisorRepository repo;
    private final PasswordEncoder encoder;

    public AdvisorService(AdvisorRepository repo, PasswordEncoder encoder)
    {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Transactional
    public Advisor saveAndLogin(Advisor advisor, String password)
    {
        advisor.setPassword(encoder.encode(password));
        Advisor saved = repo.save(advisor);
        authenticateAs(saved);
        return saved;
    }

    public void authenticateAs(Advisor advisor)
    {
        Authentication auth = new UsernamePasswordAuthenticationToken(advisor, advisor.getPassword(), advisor.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    /**
     * Check if an advisor with the given email address exists
     * @param email the advisor's email address
     * @return {@code true} if an advisor is registered with that address
     */
    public boolean advisorExists(String email)
    {
        return repo.findByEmailIgnoreCase(email).isPresent();
    }

    /**
     * Re-fetch an advisor inside a transaction.
     * Since the {@link Advisor} stored as an authentication principal is bound to whatever session
     * originally loaded it, trying to load lazy properties from it is iffy.
     * @param a An advisor
     * @return The freshly loaded advisor instance
     */
    @Transactional()
    public Advisor load(Advisor a)
    {
        Advisor advisor = repo.findOne(a.getId());
        Assert.notEmpty(advisor.getSchool().getAdvisors(), "School has no advisors");
//        Hibernate.initialize(s.getAdvisors());
        return advisor;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        return findAdvisor(username);
    }

    public Advisor findAdvisor(String email) throws UsernameNotFoundException
    {
        return repo.findByEmailIgnoreCase(email)
            .orElseThrow(() -> new UsernameNotFoundException("Advisor not found: " + email));
    }
}
