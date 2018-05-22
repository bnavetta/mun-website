package org.brownmun.util;

import java.io.IOException;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.lang.NonNull;

/**
 * A {@link PropertySourceFactory} using Spring Boot's YAML support.
 */
public class YamlPropertySourceFactory implements PropertySourceFactory
{
    @NonNull
    @Override
    public PropertySource<?> createPropertySource(String name, @NonNull EncodedResource resource) throws IOException
    {
        YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
        return loader.load(name == null ? resource.getResource().getDescription() : name, resource.getResource(), null);
    }
}
