package org.brownmun;

import com.fasterxml.jackson.datatype.guava.GuavaModule;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Common configuration and beans.
 */
@Configuration
@PropertySource(value = "file:./.env", ignoreResourceNotFound = true)
@EnableConfigurationProperties(ConferenceProperties.class)
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

    @Bean
    @Primary
    public TaskExecutor asyncTaskExecutor()
    {
        int processorCount = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(processorCount);
        executor.setThreadNamePrefix("AsyncTask-");
        executor.initialize();
        return executor;
    }
}
