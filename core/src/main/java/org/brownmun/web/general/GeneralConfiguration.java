package org.brownmun.web.general;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuation for the publicly-visible general conference information.
 */
@Configuration
@EnableConfigurationProperties(SecProperties.class)
public class GeneralConfiguration
{

}
