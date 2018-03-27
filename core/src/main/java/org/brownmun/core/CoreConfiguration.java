package org.brownmun.core;

import org.brownmun.core.secretariat.SecretariatProperties;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Objects;

@Configuration
@ComponentScan
@EntityScan
@EnableJpaRepositories
@EnableConfigurationProperties(SecretariatProperties.class)
public class CoreConfiguration
{
    @Bean
    public PropertySourcesPlaceholderConfigurer secretariatProperties()
    {
        return yamlProperties("/secretariat.yaml");
    }

    /**
     * Load a {@code core.yaml} file in addition to the default {@code application.yaml}, for properties that should
     * be always set, regardless of which conference or environment it is. This is because, AFAIK, Spring Boot will
     * only load one {@code application.yaml} file.
     */
    @Bean
    public PropertySourcesPlaceholderConfigurer coreProperties()
    {
        return yamlProperties("/core.yaml");
    }

    /*
     * Loads a YAML file as Spring Boot properties. This is kinda clunky, there
     * might be a cleaner way to do it.
     */
    private PropertySourcesPlaceholderConfigurer yamlProperties(String resource)
    {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource(resource));

        PropertySourcesPlaceholderConfigurer config = new PropertySourcesPlaceholderConfigurer();
        config.setProperties(Objects.requireNonNull(yaml.getObject()));
        return config;
    }
}
