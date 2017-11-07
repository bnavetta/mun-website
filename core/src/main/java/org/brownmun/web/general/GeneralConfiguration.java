package org.brownmun.web.general;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Configuation for the publicly-visible general conference information.
 */
@Configuration
@EnableConfigurationProperties(SecProperties.class)
public class GeneralConfiguration extends WebMvcConfigurerAdapter
{
    @Override
    public void addViewControllers(ViewControllerRegistry registry)
    {
        registry.addViewController("/about/conference").setViewName("about/conference");
        registry.addViewController("/about/brown-providence").setViewName("about/brown-providence");
        registry.addViewController("/about/sustainability").setViewName("about/sustainability");

        registry.addViewController("/registration/fees-and-deadlines").setViewName("registration/fees-and-deadlines");

        registry.addViewController("/conference/preparation").setViewName("conference/preparation");
        registry.addViewController("/conference/schedule").setViewName("conference/schedule");
        registry.addViewController("/conference/keynote-speaker").setViewName("conference/keynote-speaker");

        registry.addViewController("/logistics/directions").setViewName("logistics/directions");
        registry.addViewController("/logistics/parking").setViewName("logistics/parking");
        registry.addViewController("/logistics/restaurants").setViewName("logistics/restaurants");

        registry.addViewController("/windmill").setViewName("windmill");
    }
}
