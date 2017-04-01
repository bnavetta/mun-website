package org.brownmun.util;

import com.google.common.hash.Hashing;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Utility class for generating secure tokens.
 */
public class Tokens
{
    private static final SecureRandom random;

    static {
        try
        {
            random = SecureRandom.getInstanceStrong();
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException("Strong random number generation not supported", e);
        }
    }

    public static String generate()
    {
        byte[] buf = new byte[256];
        random.nextBytes(buf);
        return Hashing.sha256().hashBytes(buf).toString();
    }
}
