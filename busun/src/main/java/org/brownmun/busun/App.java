package org.brownmun.busun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "org.brownmun")
@SpringBootApplication
public class App
{
    public static void main(String[] args)
    {
        SpringApplication app = new SpringApplication(App.class);
        app.setAdditionalProfiles("busun");
        app.run(args);
    }
}
