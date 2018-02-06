package org.brownmun.core;

import org.brownmun.db.DatabaseConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan
@Import(DatabaseConfiguration.class)
public class CoreConfiguration
{
}
