package org.brownmun.core.school.impl;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.common.base.Throwables;

import org.brownmun.core.Conference;
import org.brownmun.core.mail.EmailMessage;
import org.brownmun.core.mail.MailException;
import org.brownmun.core.mail.MailSender;
import org.brownmun.core.school.AdvisorService;
import org.brownmun.core.school.PasswordResetException;
import org.brownmun.core.school.SchoolService;
import org.brownmun.core.school.model.Advisor;
import org.brownmun.core.school.model.PasswordReset;
import org.brownmun.core.school.model.School;
import org.brownmun.util.Tokens;

/**
 * Implementation of password resets.
 */
@Service
public class AdvisorServiceImpl implements AdvisorService
{
    private static final Logger log = LoggerFactory.getLogger(AdvisorService.class);

    private static final Duration RESET_TOKEN_LIFESPAN = Duration.ofDays(1);

    private final PasswordResetRepository repo;
    private final SchoolService schoolService;
    private final AdvisorRepository advisorRepo;
    private final MailSender mailSender;
    private final Conference conference;
    private final PasswordEncoder encoder;
    private final TransactionTemplate tx;

    @Autowired
    public AdvisorServiceImpl(PasswordResetRepository repo, SchoolService schoolService, AdvisorRepository advisorRepo,
            MailSender mailSender, Conference conference, PasswordEncoder encoder, TransactionTemplate tx)
    {
        this.repo = repo;
        this.schoolService = schoolService;
        this.advisorRepo = advisorRepo;
        this.mailSender = mailSender;
        this.conference = conference;
        this.encoder = encoder;
        this.tx = tx;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Advisor> getAdvisor(long id)
    {
        return advisorRepo.findById(id);
    }

    @Override
    public void requestPasswordReset(String email) throws PasswordResetException
    {
        String resetCode = Tokens.generate(32);
        try
        {
            // Manually manage transactions to avoid doing expensive operations like reset
            // code generation or
            // sending emails within the database transaction. Otherwise, we can starve the
            // connection pool.
            PasswordReset reset = tx.execute((t) -> {
                Advisor advisor = schoolService.findAdvisor(email)
                        .orElseThrow(() -> new RuntimeException(
                                new PasswordResetException("No advisor with that email address exists", email)));

                PasswordReset r = new PasswordReset();
                r.setResetCode(resetCode);
                r.setAdvisor(advisor);
                r.setRequestedAt(Instant.now());
                return repo.save(r);
            });

            String resetUrl = UriComponentsBuilder.newInstance()
                    .scheme("https")
                    .host(conference.getDomainName())
                    .path("/your-mun/password/reset")
                    .queryParam("token", reset.getResetCode())
                    .build()
                    .toUriString();

            EmailMessage message = EmailMessage.builder()
                    .recipient(email)
                    .subject(String.format("[%s] Password Reset", conference.getName()))
                    .messageTemplate("password-reset")
                    .variables(Map.of("advisor", reset.getAdvisor(), "resetUrl", resetUrl))
                    .build();

            mailSender.sendEmail(message);
        }
        catch (RuntimeException e)
        {
            throw Throwables.getCauseAs(e, PasswordResetException.class);
        }
        catch (MailException e)
        {
            throw new PasswordResetException("Error sending password reset email", e, email);
        }
    }

    @Override
    @Transactional
    public Advisor resetPassword(String code, String newPassword) throws PasswordResetException
    {
        try
        {
            PasswordReset reset = repo.getOne(code);

            Instant now = Instant.now();
            Instant cutoff = now.minus(RESET_TOKEN_LIFESPAN);
            if (reset.getRequestedAt().isBefore(cutoff) || reset.getRequestedAt().isAfter(now))
            {
                String advisorEmail = reset.getAdvisor().getEmail();
                repo.delete(reset);
                throw new PasswordResetException("Token has expired", advisorEmail);
            }

            Advisor advisor = reset.getAdvisor();
            advisor.setPassword(encoder.encode(newPassword));
            repo.delete(reset);
            advisor = advisorRepo.save(advisor);
            log.debug("Reset password for {}", advisor.getEmail());
            return advisor;
        }
        catch (EntityNotFoundException e)
        {
            throw new PasswordResetException("Invalid token", null);
        }
    }

    @Override
    @Transactional
    public Advisor createAdvisor(long schoolId, String name, String password, String email, String phoneNumber)
    {
        School school = schoolService.getSchool(schoolId)
                .orElseThrow(() -> new IllegalArgumentException("No school with ID " + schoolId));

        Advisor advisor = new Advisor();
        advisor.setName(name);
        advisor.setEmail(email);
        advisor.setPassword(encoder.encode(password));
        advisor.setPhoneNumber(phoneNumber);
        school.addAdvisor(advisor);
        return advisorRepo.save(advisor);
    }

    @Override
    @Transactional
    public void changePassword(Advisor advisor, String newPassword)
    {
        advisor.setPassword(encoder.encode(newPassword));
        advisorRepo.save(advisor);
        log.debug("Changed password for {}", advisor.getEmail());
    }

    @Override
    @Transactional
    public void markSeen(Advisor advisor)
    {
        advisor.setLastSeen(Instant.now());
        advisorRepo.save(advisor);
    }
}
