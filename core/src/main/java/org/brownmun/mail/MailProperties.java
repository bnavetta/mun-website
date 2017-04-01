package org.brownmun.mail;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Settings for email sending.
 */
@Data
@ConfigurationProperties("mail")
public class MailProperties
{
    String templateLocation;

    String linkBase;

    String fromAddress;
    String replyAddress;
}
