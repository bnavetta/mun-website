package org.brownmun.web.support;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

import ch.qos.logback.access.tomcat.LogbackValve;

@Component
public class TomcatCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory>
{
    @Override
    public void customize(TomcatServletWebServerFactory factory)
    {
        factory.addContextValves(new LogbackValve());
    }
}
