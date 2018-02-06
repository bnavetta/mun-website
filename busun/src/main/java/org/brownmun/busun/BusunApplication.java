package org.brownmun.busun;

import org.brownmun.core.Conference;
import org.brownmun.web.WebConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Import(WebConfiguration.class)
public class BusunApplication
{
    @Bean
    public Conference conference()
    {
        return new BusunConference();
    }

    public static void main(String[] args)
    {
        SpringApplication.run(BusunApplication.class, args);
    }
}
