package org.brownmun.advisor;

import com.google.common.collect.Lists;
import org.brownmun.mail.MailException;
import org.brownmun.mail.MailService;
import org.brownmun.model.School;
import org.brownmun.model.advisor.Advisor;
import org.brownmun.model.advisor.AdvisorCreationToken;
import org.brownmun.model.repo.AdvisorCreationTokenRepository;
import org.brownmun.util.Tokens;
import org.brownmun.web.security.AdvisorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Service to handle creating secondary {@link Advisor}s
 */
@Service
public class AdvisorCreationService
{
    private static final Logger log = LoggerFactory.getLogger(AdvisorCreationService.class);
    
    private final AdvisorCreationTokenRepository tokenRepo;
    private final AdvisorService advisorService;
    private final MailService mailService;

    @Autowired
    public AdvisorCreationService(AdvisorCreationTokenRepository tokenRepo, AdvisorService advisorService, MailService mailService)
    {
        this.tokenRepo = tokenRepo;
        this.advisorService = advisorService;
        this.mailService = mailService;
    }

    @Transactional
    public Iterable<AdvisorCreationToken> generateTokens(School school, Iterable<AdvisorCreationRequest> details)
    {
        List<AdvisorCreationToken> tokens = Lists.newArrayList();
        for (AdvisorCreationRequest advisorInfo : details)
        {
            AdvisorCreationToken token = new AdvisorCreationToken();
            token.setAdvisorEmail(advisorInfo.getEmail());
            token.setAdvisorName(advisorInfo.getName());
            token.setSchool(school);
            token.setToken(Tokens.generate());
            tokens.add(token);
        }

        log.debug("Generated {} advisor creation tokens for {}", tokens.size(), school.getName());

        return tokenRepo.save(tokens);
    }

    public void inviteAdvisors(School school, Iterable<AdvisorCreationRequest> advisors) throws AdvisorCreationException
    {
        Iterable<AdvisorCreationToken> tokens = generateTokens(school, advisors);

        try
        {
            mailService.sendAdvisorCreationInvites(tokens, school);
            log.debug("Sent invitation emails to advisors");
        }
        catch (MailException e)
        {
            log.error("Exception sending advisor invitations", e);
        }
    }

    /**
     * Register a new advisor from the given information.
     * @param token the token provided by the advisor
     * @param password the new advisor's password
     * @param base a base advisor that will be filled in with information from the token
     * @return the persisted advisor
     */
    @Transactional
    public Advisor createAdvisorAndLogin(String token, String password, Advisor base) throws AdvisorCreationException
    {
        AdvisorCreationToken dbToken = tokenRepo.findByToken(token)
            .orElseThrow(() -> new AdvisorCreationException("advisorCreation.token.missing", token));

        if (advisorService.advisorExists(dbToken.getAdvisorEmail()))
        {
            throw new AdvisorCreationException("advisorCreation.advisor.exists", token);
        }

        base.setEmail(dbToken.getAdvisorEmail());
        base.setName(dbToken.getAdvisorName());
        base.setSchool(dbToken.getSchool());
        base.setPrimary(false);

        Advisor created = advisorService.saveAndLogin(base, password);
        tokenRepo.delete(dbToken);
        return created;
    }
}
