package org.brownmun.model;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Needed so Spring Data will scan org.brownmun.model, since @SpringBootApplication is on
 * classes in org.brownmun.{bucs,busun}
 */
@Configuration
@EnableJpaRepositories
@EntityScan
public class DataConfiguration
{
}
