package org.brownmun.core;

import java.util.Objects;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import org.brownmun.core.secretariat.SecretariatProperties;
import org.brownmun.db.DatabaseConfiguration;

@Configuration
@ComponentScan
@Import(DatabaseConfiguration.class)
@EnableConfigurationProperties(SecretariatProperties.class)
public class CoreConfiguration
{
    /*
     * Loads secretariat.yaml as Spring Boot properties. This is kinda clunky, there
     * might be a cleaner way to do it.
     */
    @Bean
    public PropertySourcesPlaceholderConfigurer secretariatProperties()
    {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("/secretariat.yaml"));

        PropertySourcesPlaceholderConfigurer config = new PropertySourcesPlaceholderConfigurer();
        config.setProperties(Objects.requireNonNull(yaml.getObject()));
        return config;
    }
}
