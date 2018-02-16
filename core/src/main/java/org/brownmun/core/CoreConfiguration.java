package org.brownmun.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import org.brownmun.db.DatabaseConfiguration;

@Configuration
@ComponentScan
@Import(DatabaseConfiguration.class)
public class CoreConfiguration
{
}
