package org.brownmun.web.support.webpack;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(WebpackProperties.class)
public class WebpackConfiguration
{
    @Bean
    public ChunkResolver chunkResolver(WebpackProperties props)
    {
        return new ChunkResolver(props.getAssetBase(), props.isReload());
    }

    @Bean
    public WebpackDialect webpackDialect(ChunkResolver resolver)
    {
        return new WebpackDialect(resolver);
    }

    @Bean
    public WebpackActuatorEndpoint webpackActuatorEndpoint(ChunkResolver chunkResolver)
    {
        return new WebpackActuatorEndpoint(chunkResolver);
    }
}
