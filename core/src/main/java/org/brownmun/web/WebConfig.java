package org.brownmun.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.time.Duration;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter
{
    @Override
    public void addViewControllers(ViewControllerRegistry registry)
    {
        registry.addViewController("/").setViewName("index");

        registry.addViewController("/admin/").setViewName("admin/index");
        registry.addRedirectViewController("/admin", "/admin/");

        registry.addRedirectViewController("/yourbusun", "/yourbusun/");

        registry.addViewController("/login").setViewName("login");

        // Only useful for dev, but shouldn't break anything in production?
        registry.addRedirectViewController("/__webpack_hmr", "http://localhost:8000/__webpack_hmr");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // , "**/*.png", "/**/*.ico", "/**/*.pdf"
        registry.addResourceHandler("/**/*.js", "/**/*.css")
                .addResourceLocations("classpath:/static/", "classpath:/public/")
                .setCachePeriod(((int) Duration.ofDays(365).getSeconds()));
    }
}
