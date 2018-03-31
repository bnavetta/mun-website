package org.brownmun.web.general;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Configuation for the publicly-visible general staff information.
 */
@Configuration
@EnableConfigurationProperties(SecProperties.class)
public class GeneralConfiguration extends WebMvcConfigurerAdapter
{
    @Override
    public void addViewControllers(ViewControllerRegistry registry)
    {
        registry.addViewController("/about/staff").setViewName("about/staff");
        registry.addViewController("/about/brown-providence").setViewName("about/brown-providence");
        registry.addViewController("/about/sustainability").setViewName("about/sustainability");

        registry.addViewController("/registration/fees-and-deadlines").setViewName("registration/fees-and-deadlines");

        registry.addViewController("/staff/preparation").setViewName("staff/preparation");
        registry.addViewController("/staff/schedule").setViewName("staff/schedule");
        registry.addViewController("/staff/keynote-speaker").setViewName("staff/keynote-speaker");
        registry.addViewController("/staff/erinn-phelan").setViewName("staff/erinn-phelan");

        registry.addViewController("/logistics/directions").setViewName("logistics/directions");
        registry.addViewController("/logistics/parking").setViewName("logistics/parking");
        registry.addViewController("/logistics/restaurants").setViewName("logistics/restaurants");

        registry.addViewController("/windmill").setViewName("windmill");
    }
}
