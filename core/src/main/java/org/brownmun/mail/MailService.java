package org.brownmun.mail;

/**
 * Service abstraction for sending emails
 */
public interface MailService
{
    void send(EmailDescriptor message) throws MailException;
}
