package org.brownmun;

import com.fasterxml.jackson.datatype.guava.GuavaModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Common configuration and beans.
 */
@Configuration
@PropertySource("file:./.env")
public class CoreConfig
{
    @Bean
    public GuavaModule jacksonGuavaModule()
    {
        return new GuavaModule();
    }
}
