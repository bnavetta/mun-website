package org.brownmun.bucs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import org.brownmun.core.Conference;
import org.brownmun.web.WebConfiguration;

@SpringBootApplication
@Import(WebConfiguration.class)
public class BucsApplication
{
    public static void main(String[] args)
    {
        SpringApplication app = new SpringApplication(BucsApplication.class);
        app.setAdditionalProfiles("bucs");
        app.run(args);
    }
}
