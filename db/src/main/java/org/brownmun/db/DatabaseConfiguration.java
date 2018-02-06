package org.brownmun.db;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan
@ComponentScan
@EnableJpaRepositories
public class DatabaseConfiguration
{
}
