package org.brownmun.cli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import org.brownmun.core.CoreConfiguration;

@SpringBootApplication
@Import(CoreConfiguration.class)
public class CliApplication
{
    public static void main(String[] args)
    {
        SpringApplication app = new SpringApplication(CliApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.setAdditionalProfiles("busun");
        app.run(args);
    }
}
