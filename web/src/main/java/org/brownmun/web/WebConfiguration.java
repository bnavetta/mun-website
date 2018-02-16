package org.brownmun.web;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.brownmun.core.CoreConfiguration;

@Configuration
@ComponentScan
@Import(CoreConfiguration.class)
public class WebConfiguration implements WebMvcConfigurer
{
    @Override
    public void addViewControllers(ViewControllerRegistry registry)
    {
        registry.addViewController("/about/brown-busun").setViewName("about/brown-busun");
        registry.addViewController("/about/secretariat").setViewName("about/secretariat");

        registry.addViewController("/registration/application").setViewName("registration/application");
        registry.addViewController("/registration/fees-and-deadlines").setViewName("registration/fees-and-deadlines");
        registry.addViewController("/registration/financial-aid").setViewName("registration/financial-aid");

        registry.addViewController("/schedule").setViewName("schedule");

        registry.addViewController("/conference-resources/preparation-and-procedure")
                .setViewName("conference-resources/preparation-and-procedure");
        registry.addViewController("/conference-resources/delegate-guide")
                .setViewName("conference-resources/delegate-guide");
        registry.addViewController("/conference-resources/erinn-phelan-award")
                .setViewName("conference-resources/erinn-phelan-award");

        registry.addViewController("/logistics/getting-to-campus").setViewName("logistics/getting-to-campus");
        registry.addViewController("/logistics/shuttles-and-parking").setViewName("logistics/shuttles-and-parking");
        registry.addViewController("/logistics/local-restaurants").setViewName("logistics/local-restaurants");
    }
}
