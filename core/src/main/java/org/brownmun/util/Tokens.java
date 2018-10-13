package org.brownmun.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import io.sentry.Sentry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for generating secure tokens.
 */
public class Tokens
{
    private static final Logger log = LoggerFactory.getLogger(Tokens.class);
    private static final SecureRandom random;
    private static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    static
    {
        try
        {
            random = SecureRandom.getInstanceStrong();
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException("Strong random number generation not supported", e);
        }
    }

    /**
     * Create a random alphanumeric token of a fixed length
     *
     * @param length length of the token
     * @return a random token
     */
    public static String generate(int length)
    {
        StringBuilder buf = new StringBuilder(length);
        int alphabetLength = alphabet.length();

        long start = System.currentTimeMillis();
        for (int i = 0; i < length; i++)
        {

            buf.append(alphabet.charAt(random.nextInt(alphabetLength)));
        }
        long end = System.currentTimeMillis();

        if (end - start > 5000)
        {
            // Ran into a tricky-to-debug issue once where the system random number
            // generator (/dev/random) was low on
            // entropy, so token generation was *super* slow.
            log.error("Slow random number generation: {}ms", end - start);
            Sentry.capture(String.format("Slow random number generation: %d ms", end - start));
        }

        return buf.toString();
    }
}
