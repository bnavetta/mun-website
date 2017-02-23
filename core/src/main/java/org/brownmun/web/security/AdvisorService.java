package org.brownmun.web.security;

import org.brownmun.model.Advisor;
import org.brownmun.model.repo.AdvisorRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import javax.validation.ConstraintViolationException;

@Service
public class AdvisorService implements UserDetailsService
{
    private final AdvisorRepository repo;
    private final PasswordEncoder encoder;

    public AdvisorService(AdvisorRepository repo, PasswordEncoder encoder)
    {
        this.repo = repo;
        this.encoder = encoder;
    }

    public Optional<Advisor> register(Advisor advisor)
    {
        advisor.setPassword(encoder.encode(advisor.getPassword()));
        try
        {
            return Optional.of(repo.save(advisor));
        }
        catch (DuplicateKeyException e)
        {
            return Optional.empty();
        }
    }

    public void authenticateAs(Advisor advisor)
    {
        Authentication auth = new UsernamePasswordAuthenticationToken(advisor, advisor.getPassword(), advisor.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        return repo.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("Advisor not found: " + username));
    }
}
