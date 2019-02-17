package org.brownmun.core;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.brownmun.core.staff.SecretariatProperties;
import org.brownmun.util.YamlPropertySourceFactory;

@Configuration
@ComponentScan
@EntityScan
@EnableJpaRepositories
@EnableConfigurationProperties({ SecretariatProperties.class, Conference.class })
@PropertySource(value = "classpath:/staff.yaml", factory = YamlPropertySourceFactory.class)
@PropertySource(value = "classpath:/core.yaml", factory = YamlPropertySourceFactory.class)
public class CoreConfiguration
{

}
