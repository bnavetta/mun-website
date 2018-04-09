package org.brownmun.core.mail;

/**
 * An exception from sending an email.
 */
public class MailException extends Exception
{
    private final EmailMessage email;

    public MailException(String message, EmailMessage email)
    {
        super(message);
        this.email = email;
    }

    public MailException(String message, Throwable cause, EmailMessage email)
    {
        super(message, cause);
        this.email = email;
    }

    public MailException(Throwable cause, EmailMessage email)
    {
        super(cause);
        this.email = email;
    }

    /**
     * The email that caused the error.
     */
    public EmailMessage getEmail()
    {
        return email;
    }
}
