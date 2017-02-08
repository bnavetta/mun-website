package org.brownmun.web;

import com.fasterxml.jackson.datatype.guava.GuavaModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Customize JSON (de)serialization
 */
@Configuration
public class JacksonConfig
{
	@Bean
	public GuavaModule jacksonGuavaModule()
	{
		return new GuavaModule();
	}
}
