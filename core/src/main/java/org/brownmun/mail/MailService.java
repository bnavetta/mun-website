package org.brownmun.mail;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.brownmun.model.Advisor;
import org.brownmun.model.AdvisorCreationToken;
import org.brownmun.model.PasswordResetToken;
import org.brownmun.model.School;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Service wrapper around all the transactional emails we have to send.
 */
@Slf4j
@Service
public class MailService
{
    private final MailSender mailSender;
    private final MessageLoader messageLoader;
    private final MailProperties props;
    private final MessageSource messages;

    @Autowired
    public MailService(MailSender mailSender, MessageLoader messageLoader, MailProperties props, MessageSource messages)
    {
        this.mailSender = mailSender;
        this.messageLoader = messageLoader;
        this.props = props;
        this.messages = messages;
    }

    public void sendAdvisorCreationInvites(Iterable<AdvisorCreationToken> advisors, School school) throws MailException
    {
        Map<String, Map<String, String>> recipients = Maps.newHashMap();
        for (AdvisorCreationToken advisorToken : advisors)
        {
            Map<String, String> variables = Maps.newHashMap();
            variables.put("name", advisorToken.getAdvisorName());
            variables.put("school", school.getName());
            String registerUrl = UriComponentsBuilder.fromHttpUrl(props.getLinkBase())
                .path("/yourbusun/add-advisors/confirm")
                .queryParam("token", advisorToken.getToken())
                .build().toUriString();
            variables.put("registerUrl", registerUrl);
            recipients.put(advisorToken.getAdvisorEmail(), variables);
        }

        EmailDescriptor message = new EmailDescriptor();
        message.setFrom(props.getFromAddress());
        message.setReplyTo(Optional.of(props.getReplyAddress()));
        message.setRecipients(recipients);
        message.setSubject(messages.getMessage("email.advisorCreation.subject", null, Locale.getDefault()));
        message.setHtml(messageLoader.getMessage("new-advisor"));
        mailSender.send(message);
        log.debug("Sent advisor invites to {}", recipients);
    }

    public void sendPasswordReset(PasswordResetToken token) throws MailException
    {
        EmailDescriptor message = new EmailDescriptor();
        message.setSubject(messages.getMessage("email.passwordReset.subject", null, Locale.getDefault()));
        message.setRecipients(ImmutableMap.of(token.getAdvisor().getEmail(), ImmutableMap.of(
            "name", token.getAdvisor().getName(),
            "resetUrl", UriComponentsBuilder.fromHttpUrl(props.getLinkBase())
                .path("advisor/reset-password")
                .queryParam("token", token.getResetCode())
                .build().toUriString()
        )));
        message.setFrom(props.getFromAddress());
        message.setReplyTo(Optional.of(props.getReplyAddress()));
        message.setTags(Sets.newHashSet("password-reset", "advisor"));
        message.setHtml(messageLoader.getMessage("reset-password"));
        mailSender.send(message);
        log.debug("Sent password reset email to {}", token.getAdvisor().getEmail());
    }

    public void sendRegistrationConfirmation(School school, Advisor advisor) throws MailException
    {
        EmailDescriptor message = new EmailDescriptor();
        message.setSubject(messages.getMessage("email.registrationConfirmation.subject", null, Locale.getDefault()));
        Map<String, String> variables = Maps.newHashMap();
        variables.put("name", advisor.getName());
        variables.put("schoolName", school.getName());
        variables.put("schoolAddress", school.getAddress().toString());
        variables.put("registrationTime", school.getRegistrationTime().toString());
        variables.put("delegateCount", school.getRequestedDelegates().toString());
        variables.put("chaperoneCount", school.getRequestedChaperones().toString());
        message.setRecipients(ImmutableMap.of(advisor.getEmail(), variables));
        message.setFrom(props.getFromAddress());
        message.setReplyTo(Optional.of(props.getReplyAddress()));
        message.setTags(Sets.newHashSet("registration", "advisor"));
        message.setHtml(messageLoader.getMessage("registration-confirmation"));
        mailSender.send(message);
        log.debug("Sent registration confirmation to {}", advisor.getEmail());
    }
}
