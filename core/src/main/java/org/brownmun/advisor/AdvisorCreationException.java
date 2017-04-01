package org.brownmun.advisor;

/**
 * An exception produced when creating a new advisor
 */
public class AdvisorCreationException extends Exception
{
    private final String messageCode;
    private final String token;

    public AdvisorCreationException(String messageCode, String token, Throwable cause)
    {
        super(String.format("%s: %s", token, messageCode), cause);
        this.messageCode = messageCode;
        this.token = token;
    }

    public AdvisorCreationException(String messageCode, String token)
    {
        this(messageCode, token, null);
    }

    public String getMessageCode()
    {
        return messageCode;
    }

    public String getToken()
    {
        return token;
    }
}
