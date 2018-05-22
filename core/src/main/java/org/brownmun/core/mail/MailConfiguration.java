package org.brownmun.core.mail;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import org.brownmun.core.mail.impl.MailgunClient;
import org.brownmun.core.mail.impl.MailgunMailSender;
import org.brownmun.core.mail.impl.MailgunProperties;

@Configuration
@EnableConfigurationProperties({ MailProperties.class, MailgunProperties.class })
public class MailConfiguration
{
    @Autowired
    private ApplicationContext context;

    @Autowired
    private ThymeleafProperties thymeleafProperties;

    private SpringResourceTemplateResolver textTemplateResolver()
    {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(context);
        resolver.setPrefix("classpath:/emails/");
        resolver.setSuffix("txt");
        resolver.setTemplateMode(TemplateMode.TEXT);

        resolver.setCacheable(thymeleafProperties.isCache());
        resolver.setCheckExistence(thymeleafProperties.isCheckTemplate());
        if (thymeleafProperties.getEncoding() != null)
        {
            resolver.setCharacterEncoding(thymeleafProperties.getEncoding().name());
        }
        return resolver;
    }

    private SpringResourceTemplateResolver htmlTemplateResolver()
    {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(context);
        resolver.setPrefix("classpath:/emails/");
        resolver.setSuffix("html");
        resolver.setTemplateMode(TemplateMode.HTML);

        resolver.setCacheable(thymeleafProperties.isCache());
        resolver.setCheckExistence(thymeleafProperties.isCheckTemplate());
        if (thymeleafProperties.getEncoding() != null)
        {
            resolver.setCharacterEncoding(thymeleafProperties.getEncoding().name());
        }
        return resolver;
    }

    @Bean
    public TemplateEngine emailTemplateEngine(Collection<IDialect> dialects)
    {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setEnableSpringELCompiler(thymeleafProperties.isEnableSpringElCompiler());
        dialects.forEach(engine::addDialect);
        engine.addTemplateResolver(htmlTemplateResolver());
        engine.addTemplateResolver(textTemplateResolver());

        return engine;
    }

    @Bean
    public MailgunMailSender mailSender(@Qualifier("emailTemplateEngine") TemplateEngine engine, MailgunClient client,
            MailProperties mailProperties)
    {
        return new MailgunMailSender(mailProperties, engine, client);
    }

    @Bean
    public MailgunClient mailgunClient(MailgunProperties props, RestTemplateBuilder builder)
    {
        return new MailgunClient(props, builder);
    }
}
