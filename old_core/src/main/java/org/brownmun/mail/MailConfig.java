package org.brownmun.mail;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.brownmun.mail.mailgun.MailgunMailSender;
import org.brownmun.mail.mailgun.MailgunProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

/**
 * Spring configuration for sending emails
 */
@Configuration
@EnableConfigurationProperties({ MailgunProperties.class, MailProperties.class })
public class MailConfig
{
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder, MailgunProperties mailgun)
    {
        return builder
            .basicAuthorization(mailgun.getUsername(), mailgun.getApiKey())
            .rootUri(mailgun.getBaseUri())
            .build();
    }

    @Bean
    public MailSender mailgun(RestTemplate restTemplate, ObjectMapper mapper)
    {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(5);
        executor.setThreadNamePrefix("Mail-");
        executor.initialize();

        return new MailgunMailSender(restTemplate, mapper, executor);
    }

    @Bean
    public MessageLoader messageLoader(MailProperties props, ResourceLoader loader)
    {
        return new MessageLoader(loader, props.getTemplateLocation());
    }
}
