package org.brownmun.web.general;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Configuation for the publicly-visible general secretariat information.
 */
@Configuration
@EnableConfigurationProperties(SecProperties.class)
public class GeneralConfiguration extends WebMvcConfigurerAdapter
{
    @Override
    public void addViewControllers(ViewControllerRegistry registry)
    {
        registry.addViewController("/about/secretariat").setViewName("about/secretariat");
        registry.addViewController("/about/brown-providence").setViewName("about/brown-providence");
        registry.addViewController("/about/sustainability").setViewName("about/sustainability");

        registry.addViewController("/registration/fees-and-deadlines").setViewName("registration/fees-and-deadlines");

        registry.addViewController("/secretariat/preparation").setViewName("secretariat/preparation");
        registry.addViewController("/secretariat/schedule").setViewName("secretariat/schedule");
        registry.addViewController("/secretariat/keynote-speaker").setViewName("secretariat/keynote-speaker");
        registry.addViewController("/secretariat/erinn-phelan").setViewName("secretariat/erinn-phelan");

        registry.addViewController("/logistics/directions").setViewName("logistics/directions");
        registry.addViewController("/logistics/parking").setViewName("logistics/parking");
        registry.addViewController("/logistics/restaurants").setViewName("logistics/restaurants");

        registry.addViewController("/windmill").setViewName("windmill");
    }
}
