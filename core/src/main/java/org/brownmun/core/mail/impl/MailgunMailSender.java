package org.brownmun.core.mail.impl;

import java.time.Duration;
import java.time.Instant;
import java.util.Locale;

import io.sentry.Sentry;
import io.sentry.event.EventBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.exceptions.TemplateEngineException;

import org.brownmun.core.mail.EmailMessage;
import org.brownmun.core.mail.MailException;
import org.brownmun.core.mail.MailProperties;
import org.brownmun.core.mail.MailSender;

/**
 * Mailgun-based {@link MailSender}
 */
public class MailgunMailSender implements MailSender
{
    private static final Logger log = LoggerFactory.getLogger(MailgunMailSender.class);

    private final MailProperties mailProperties;
    private final TemplateEngine engine;
    private final MailgunClient client;

    public MailgunMailSender(MailProperties mailProperties, TemplateEngine engine, MailgunClient client)
    {
        this.mailProperties = mailProperties;
        this.engine = engine;
        this.client = client;
    }

    @Override
    public void sendEmail(EmailMessage email) throws MailException
    {
        Context context = new Context(Locale.getDefault(), email.variables());
        try
        {
            String text = engine.process(email.messageTemplate() + ".txt", context);
            String html = engine.process(email.messageTemplate() + ".html", context);

            Instant start = Instant.now();
            client.send(mailProperties.getFromAddress(), email.recipient(), mailProperties.getReplyToAddress(),
                    email.subject(), html, text);
            Instant end = Instant.now();
            Duration sendTime = Duration.between(start, end);
            log.debug("Sent email [{}] to {} in {}ms", email.subject(), email.recipient(), sendTime);
            if (sendTime.toSeconds() > 5)
            {
                Sentry.capture(new EventBuilder()
                        .withMessage(String.format("Slow Mailgun call: %d ms", sendTime.toMillis()))
                        .withExtra("subject", email.subject())
                        .withExtra("recipient", email.recipient()));
            }
        }
        catch (TemplateEngineException e)
        {
            throw new MailException("Error processing email template", e, email);
        }
        catch (RestClientException e)
        {
            throw new MailException("Error submitting email", e, email);
        }
    }
}
