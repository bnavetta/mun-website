package org.brownmun.web.security;

import com.google.common.hash.Hashing;
import org.brownmun.model.Advisor;
import org.brownmun.model.NewAdvisorToken;
import org.brownmun.model.School;
import org.brownmun.model.repo.AdvisorRepository;
import org.brownmun.model.repo.NewAdvisorTokenRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

@Service
public class AdvisorService implements UserDetailsService
{
    private final SecureRandom random;

    private final AdvisorRepository repo;
    private final NewAdvisorTokenRepository natRepo;
    private final PasswordEncoder encoder;

    public AdvisorService(AdvisorRepository repo, NewAdvisorTokenRepository natRepo, PasswordEncoder encoder)
    {
        this.repo = repo;
        this.natRepo = natRepo;
        this.encoder = encoder;

        try
        {
            this.random = SecureRandom.getInstanceStrong();
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public Optional<Advisor> register(Advisor advisor)
    {
        if (existingAdvisor(advisor.getEmail()))
        {
            // TODO: show this to the user
            return Optional.empty();
        }

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

    /**
     * Check if an advisor with the given email address exists
     * @param email
     * @return
     */
    private boolean existingAdvisor(String email)
    {
        return repo.findByEmail(email).isPresent();
    }

    // TODO: move advisor registration logic into a separate service and be consistent about terminology (call it advisor registration)

    private String generateToken()
    {
        byte[] buf = new byte[256];
        random.nextBytes(buf);
        return Hashing.sha256().hashBytes(buf).toString();
    }

    @Transactional
    public Iterable<NewAdvisorToken> createTokens(School school, Collection<AdvisorRequest> requests)
    {
        List<NewAdvisorToken> tokens = requests.stream().map(request -> {
            NewAdvisorToken token = new NewAdvisorToken();
            token.setAdvisorEmail(request.getEmail());
            token.setAdvisorName(request.getName());
            token.setSchool(school);
            token.setToken(generateToken());
            return token;
        }).collect(Collectors.toList());

        return natRepo.save(tokens);
    }

    @Transactional
    public Optional<Advisor> createAdvisorFromToken(String token, String password, String phoneNumber)
    {
        return natRepo.findByToken(token).flatMap(nat -> {
            if (existingAdvisor(nat.getAdvisorEmail()))
            {
                // TODO: actually indicate this to the user
                return Optional.empty();
            }

            Advisor advisor = new Advisor();
            advisor.setEmail(nat.getAdvisorEmail());
            advisor.setName(nat.getAdvisorName());
            advisor.setPassword(encoder.encode(password));
            advisor.setPhoneNumber(phoneNumber);
            advisor.setPrimary(false);
            advisor.setSchool(nat.getSchool());

            try
            {
                Advisor created = repo.save(advisor);
                natRepo.delete(nat);
                return Optional.of(created);
            }
            catch (DuplicateKeyException e)
            {
                return Optional.empty();
            }
        });
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
        Assert.notEmpty(advisor.getSchool().getAdvisors());
//        Hibernate.initialize(s.getAdvisors());
        return advisor;
    }

    @Transactional
    public Advisor changePassword(Advisor advisor, String newPassword)
    {
        advisor.setPassword(encoder.encode(newPassword));
        Advisor persisted = repo.save(advisor);
        authenticateAs(persisted);
        return persisted;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        return repo.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("Advisor not found: " + username));
    }
}
