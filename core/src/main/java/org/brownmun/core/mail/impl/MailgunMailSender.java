package org.brownmun.core.mail.impl;

import org.brownmun.core.Conference;
import org.brownmun.core.mail.EmailMessage;
import org.brownmun.core.mail.MailException;
import org.brownmun.core.mail.MailProperties;
import org.brownmun.core.mail.MailSender;
import org.springframework.web.client.RestClientException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.exceptions.TemplateEngineException;

import java.util.Locale;

/**
 * Mailgun-based {@link MailSender}
 */
public class MailgunMailSender implements MailSender
{
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

            client.send(mailProperties.getFromAddress(), email.recipient(), mailProperties.getReplyToAddress(), email.subject(), html, text);
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
