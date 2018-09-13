package org.brownmun.util;

import java.io.IOException;
import java.util.Objects;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.lang.NonNull;

import com.google.common.collect.Iterables;

/**
 * A {@link PropertySourceFactory} using Spring Boot's YAML support.
 */
public class YamlPropertySourceFactory implements PropertySourceFactory
{
    @NonNull
    @Override
    public PropertySource<?> createPropertySource(String name, @NonNull EncodedResource resource) throws IOException
    {
//        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
//        factory.setResources(resource.getResource());
//        factory.afterPropertiesSet();
//
//        String sourceName = name != null ? name : resource.getResource().getDescription();
//        return new PropertiesPropertySource(sourceName, Objects.requireNonNull(factory.getObject()));

        YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
        return Iterables.getOnlyElement(
                loader.load(name == null ? resource.getResource().getDescription() : name, resource.getResource()));
    }
}
