package org.brownmun.advisor;

/**
 * An exception produced when resetting an advisor's password.
 */
public class PasswordResetException extends Exception
{
    private final String messageCode;
    private final String email;

    public PasswordResetException(String messageCode, String email, Throwable cause)
    {
        super(String.format("%s: %s", email, messageCode), cause);
        this.messageCode = messageCode;
        this.email = email;
    }

    public PasswordResetException(String messageCode, String email)
    {
        this(messageCode, email, null);
    }

    public String getMessageCode()
    {
        return messageCode;
    }

    public String getEmail()
    {
        return email;
    }
}
