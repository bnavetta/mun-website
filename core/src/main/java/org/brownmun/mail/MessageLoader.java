package org.brownmun.mail;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.io.CharStreams;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

/**
 * Helper for loading email message templates from files.
 */
public class MessageLoader
{
    private final ResourceLoader resourceLoader;

    private final LoadingCache<String, String> messageCache;

    public MessageLoader(ResourceLoader resourceLoader, String messageBase)
    {
        this.resourceLoader = resourceLoader;

        this.messageCache = CacheBuilder.newBuilder()
            .maximumSize(20)
            .build(new CacheLoader<String, String>()
            {
                @Override
                public String load(String key) throws Exception
                {
                    Resource resource = resourceLoader.getResource(messageBase + key);
                    try (InputStream in = resource.getInputStream())
                    {
                        return CharStreams.toString(new InputStreamReader(in, StandardCharsets.UTF_8));
                    }
                }
            });
    }

    public String getMessage(String name)
    {
        // Should mostly be classpath resources that we don't expect to have any trouble loading
        try
        {
            return messageCache.get(name);
        }
        catch (ExecutionException e)
        {
            throw new RuntimeException(e);
        }
    }
}
