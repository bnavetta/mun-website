package org.brownmun.core.mail;

/**
 * A service for sending emails
 */
public interface MailSender
{
    /**
     * Sends an email.
     *
     * @param email the email to send
     * @throws MailException if sending the email fails
     */
    void sendEmail(EmailMessage email) throws MailException;
}
