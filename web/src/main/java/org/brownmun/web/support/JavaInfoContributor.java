package org.brownmun.web.support;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;

public class JavaInfoContributor implements InfoContributor
{
    @Override
    public void contribute(Info.Builder builder)
    {
        builder.withDetail("java-version", System.getProperty("java.runtime.version"))
                .withDetail("java-vendor", System.getProperty("java.vm.vendor"))
                .withDetail("java-runtime", System.getProperty("java.vm.name"));
    }
}
