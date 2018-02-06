package org.brownmun.mail;

/**
 * Service abstraction for sending emails
 */
public interface MailSender
{
    void send(EmailDescriptor message) throws MailException;
}
