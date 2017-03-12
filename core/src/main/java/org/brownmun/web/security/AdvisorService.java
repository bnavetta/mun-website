package org.brownmun.web.security;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.brownmun.mail.EmailDescriptor;
import org.brownmun.mail.MailException;
import org.brownmun.mail.MailService;
import org.brownmun.mail.MessageLoader;
import org.brownmun.model.Advisor;
import org.brownmun.model.NewAdvisorToken;
import org.brownmun.model.PasswordResetToken;
import org.brownmun.model.School;
import org.brownmun.model.repo.AdvisorRepository;
import org.brownmun.model.repo.NewAdvisorTokenRepository;
import org.brownmun.model.repo.PasswordResetTokenRepository;
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
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

@Slf4j
@Service
public class AdvisorService implements UserDetailsService
{
    private static final Duration RESET_TOKEN_LIFESPAN = Duration.ofDays(1);

    private final SecureRandom random;

    private final AdvisorRepository repo;
    private final NewAdvisorTokenRepository natRepo;
    private final PasswordResetTokenRepository resetRepo;
    private final PasswordEncoder encoder;
    private final MailService mailService;
    private final MessageLoader messageLoader;

    public AdvisorService(AdvisorRepository repo, NewAdvisorTokenRepository natRepo, PasswordResetTokenRepository resetRepo, PasswordEncoder encoder, MailService mailService, MessageLoader messageLoader)
    {
        this.repo = repo;
        this.natRepo = natRepo;
        this.resetRepo = resetRepo;
        this.encoder = encoder;
        this.mailService = mailService;
        this.messageLoader = messageLoader;

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

    @Transactional
    public Optional<String> requestPasswordReset(String email)
    {
        Optional<Advisor> a = repo.findByEmail(email);
        if (a.isPresent())
        {
            Advisor advisor = a.get();
            if (!resetRepo.findByAdvisor(advisor).isEmpty())
            {
                return Optional.of("Password reset already requested");
            }

            PasswordResetToken token = new PasswordResetToken();
            token.setResetCode(generateToken());
            token.setAdvisor(advisor);
            token.setRequestedAt(Instant.now());
            token = resetRepo.save(token);

            EmailDescriptor resetEmail = new EmailDescriptor();
            resetEmail.setSubject("BUSUN Password Reset");
            resetEmail.setRecipients(ImmutableMap.of(email, ImmutableMap.of(
                "name", advisor.getName(),
                "resetUrl", "http://localhost:8080/advisor/reset-password?token=" + token.getResetCode()
            )));
            // TODO: put these in config
            resetEmail.setFrom("admin@busun.org");
            resetEmail.setReplyTo(Optional.of("technology@busun.org"));
            resetEmail.setTags(Sets.newHashSet("password-reset", "advisor"));
            resetEmail.setHtml(messageLoader.getMessage("reset-password "));
            try
            {
                mailService.send(resetEmail);
            }
            catch (MailException e)
            {
                log.error("Error sending password reset email", e);
                return Optional.of("Unable to send reset email");
            }

            return Optional.empty();
        }
        else
        {
            return Optional.of("No advisor with that email address found");
        }
    }

    @Transactional
    public Optional<String> resetPassword(String code, String newPassword)
    {
        PasswordResetToken token = resetRepo.findOne(code);
        if (token == null)
        {
            return Optional.of("Invalid reset code");
        }

        Instant now = Instant.now();
        Instant cutoff = now.minus(RESET_TOKEN_LIFESPAN);

        if (token.getRequestedAt().isBefore(cutoff) || token.getRequestedAt().isAfter(now))
        {
            resetRepo.delete(token);
            return Optional.of("Reset code has expired");
        }

        Advisor advisor = token.getAdvisor();
        advisor.setPassword(encoder.encode(newPassword));
        authenticateAs(repo.save(advisor));
        resetRepo.delete(token);

        return Optional.empty();
    }
}
