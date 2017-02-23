package org.brownmun;

import com.fasterxml.jackson.datatype.guava.GuavaModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    /*
     * Makes more sense in WebSecurityConfig but that leads to a dependency cycle because
     * AdvisorService needs the password encoder and the security confing needs AdvisorService
     */
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
