package org.brownmun.util;

import com.google.common.hash.Hashing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Utility class for generating secure tokens.
 */
public class Tokens
{
    private static final Logger log = LoggerFactory.getLogger(Tokens.class);
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
        long start = System.currentTimeMillis();
        random.nextBytes(buf);
        long end = System.currentTimeMillis();
        if (end - start > 5000)
        {
            log.error("Slow random number generation: {}ms", end - start);
        }
        String hash = Hashing.sha256().hashBytes(buf).toString();
        return hash;
    }
}
