package org.brownmun.core.school;

/**
 * An exception from attempting a password reset.
 */
public class PasswordResetException extends Exception
{
    private final String email;

    public PasswordResetException(String message, String email)
    {
        super(message);
        this.email = email;
    }

    public PasswordResetException(String message, Throwable cause, String email)
    {
        super(message, cause);
        this.email = email;
    }

    /**
     * The email address of the advisor trying to reset their password
     */
    public String getEmail()
    {
        return email;
    }
}
