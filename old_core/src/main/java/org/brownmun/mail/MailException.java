package org.brownmun.mail;

/**
 * An exception from sending an email
 */
public class MailException extends Exception
{
    private EmailDescriptor email;

    public MailException(String message, EmailDescriptor email)
    {
        super(message);
        this.email = email;
    }

    public MailException(String message, Throwable cause, EmailDescriptor email)
    {
        super(message, cause);
        this.email = email;
    }

    public EmailDescriptor getEmail()
    {
        return email;
    }
}
