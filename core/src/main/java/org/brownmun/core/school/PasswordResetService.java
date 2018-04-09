package org.brownmun.core.school;

import org.brownmun.core.Conference;
import org.brownmun.core.mail.EmailMessage;
import org.brownmun.core.mail.MailException;
import org.brownmun.core.mail.MailSender;
import org.brownmun.core.school.model.Advisor;
import org.brownmun.core.school.model.AdvisorRepository;
import org.brownmun.core.school.model.PasswordReset;
import org.brownmun.core.school.model.PasswordResetRepository;
import org.brownmun.util.Tokens;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

/**
 * Implementation of password resets.
 */
@Service
public class PasswordResetService
{
    private static final Logger log = LoggerFactory.getLogger(PasswordResetService.class);

    private static final Duration RESET_TOKEN_LIFESPAN = Duration.ofDays(1);

    private final PasswordResetRepository repo;
    private final SchoolService schoolService;
    private final AdvisorRepository advisorRepo;
    private final MailSender mailSender;
    private final Conference conference;
    private final PasswordEncoder encoder;

    @Autowired
    public PasswordResetService(PasswordResetRepository repo, SchoolService schoolService, AdvisorRepository advisorRepo, MailSender mailSender, Conference conference, PasswordEncoder encoder)
    {
        this.repo = repo;
        this.schoolService = schoolService;
        this.advisorRepo = advisorRepo;
        this.mailSender = mailSender;
        this.conference = conference;
        this.encoder = encoder;
    }

    @Transactional
    public void requestPasswordReset(String email) throws PasswordResetException
    {
        Advisor advisor = schoolService.findAdvisor(email).orElseThrow(() -> new PasswordResetException("No advisor with that email address exists", email));

        PasswordReset reset = new PasswordReset();
        reset.setResetCode(Tokens.generate(32));
        reset.setAdvisor(advisor);
        reset.setRequestedAt(Instant.now());
        reset = repo.save(reset);

        String resetUrl = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(conference.getDomainName())
                .path("/your-mun/password/reset")
                .queryParam("token", reset.getResetCode())
                .build().toUriString();

        EmailMessage message = EmailMessage.builder()
                .recipient(email)
                .subject(String.format("[%s] Password Reset", conference.getName()))
                .messageTemplate("password-reset")
                .variables(Map.of("advisor", advisor, "resetUrl", resetUrl))
                .build();

        try
        {
            mailSender.sendEmail(message);
        }
        catch (MailException e)
        {
            throw new PasswordResetException("Error sending password reset email", e, email);
        }
    }

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
}
