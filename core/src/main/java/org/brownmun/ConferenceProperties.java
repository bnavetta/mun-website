package org.brownmun;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("conference")
public class ConferenceProperties
{
    private String name;

    private boolean decisionsPublic;
}
