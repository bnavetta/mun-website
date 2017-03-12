package org.brownmun.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Slf4j
@Configuration
public class ViewConfig extends WebMvcConfigurerAdapter
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
}
