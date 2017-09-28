package org.brownmun.advisor;

import org.brownmun.mail.MailException;
import org.brownmun.mail.MailService;
import org.brownmun.model.advisor.Advisor;
import org.brownmun.model.advisor.PasswordResetToken;
import org.brownmun.model.repo.PasswordResetTokenRepository;
import org.brownmun.util.Tokens;
import org.brownmun.web.security.AdvisorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import javax.transaction.Transactional;

/**
 * Service for handling password resets
 */
@Service
public class PasswordResetService
{
    private static final Logger log = LoggerFactory.getLogger(PasswordResetService.class);

    // TODO: put in config?
    private static final Duration RESET_TOKEN_LIFESPAN = Duration.ofDays(1);

    private final AdvisorService advisorService;
    private final MailService mailService;
    private final PasswordResetTokenRepository tokenRepo;

    public PasswordResetService(AdvisorService advisorService, MailService mailService, PasswordResetTokenRepository tokenRepo)
    {
        this.advisorService = advisorService;
        this.mailService = mailService;
        this.tokenRepo = tokenRepo;
    }

    @Transactional
    public void requestPasswordReset(String email) throws PasswordResetException
    {
        try
        {
            Advisor advisor = advisorService.findAdvisor(email);

            PasswordResetToken token = new PasswordResetToken();
            token.setResetCode(Tokens.generate());
            token.setAdvisor(advisor);
            token.setRequestedAt(Instant.now());
            token = tokenRepo.save(token);

            mailService.sendPasswordReset(token);
        }
        catch (UsernameNotFoundException e)
        {
            throw new PasswordResetException("passwordReset.advisor.missing", email, e);
        }
        catch (MailException e)
        {
            log.error("Error sending password reset email", e);
            throw new PasswordResetException("passwordReset.emailFailed", email, e);
        }
    }

    public Advisor resetPasswordAndLogin(String code, String newPassword) throws PasswordResetException
    {
        PasswordResetToken token = tokenRepo.findOne(code);
        if (token == null)
        {
            throw new PasswordResetException("passwordReset.code.invalid", null);
        }

        Instant now = Instant.now();
        Instant cutoff = now.minus(RESET_TOKEN_LIFESPAN);

        if (token.getRequestedAt().isBefore(cutoff) || token.getRequestedAt().isAfter(now))
        {
            String advisorEmail = token.getAdvisor().getEmail();
            tokenRepo.delete(token);
            throw new PasswordResetException("passwordReset.token.expired", advisorEmail);
        }

        Advisor advisor = advisorService.saveAndLogin(token.getAdvisor(), newPassword);
        tokenRepo.delete(token);
        log.debug("Reset password for {}", advisor.getEmail());
        return advisor;
    }
}
