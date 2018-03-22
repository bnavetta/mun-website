package org.brownmun.web;

import org.brownmun.ConferenceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.time.Duration;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter
{
    @Autowired
    private ConferenceProperties conference;

    @Override
    public void addViewControllers(ViewControllerRegistry registry)
    {
        registry.addViewController("/").setViewName("index");

        registry.addViewController("/admin/").setViewName("admin/index");
        registry.addRedirectViewController("/admin", "/admin/");

        registry.addRedirectViewController("/yourmun", "/yourmun/");

        // Migrate old paths
        registry.addRedirectViewController("/yourbusun/{path}", "/yourmun/{path}");
        registry.addRedirectViewController("/yourbusun", "/yourmun/");
        registry.addRedirectViewController("/yourbusun/", "/yourmun/");

        registry.addViewController("/login").setViewName("login");

        // Only useful for dev, but shouldn't break anything in production?
        registry.addRedirectViewController("/__webpack_hmr", "http://localhost:8000/__webpack_hmr");

        if (conference.getName().equals("BUSUN"))
        {
            registry.addViewController("/secretariat/classes").setViewName("secretariat/classes");
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // , "**/*.png", "/**/*.ico", "/**/*.pdf"
        registry.addResourceHandler("/**/*.js", "/**/*.css")
                .addResourceLocations("classpath:/static/", "classpath:/public/")
                .setCachePeriod(((int) Duration.ofDays(365).getSeconds()));
    }
}
