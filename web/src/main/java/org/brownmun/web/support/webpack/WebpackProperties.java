package org.brownmun.web.support.webpack;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@ConfigurationProperties("webpack")
public class WebpackProperties
{
    private URI assetBase;
    private boolean reload;

    public URI getAssetBase()
    {
        return assetBase;
    }

    public void setAssetBase(URI assetBase)
    {
        this.assetBase = assetBase;
    }

    public boolean isReload()
    {
        return reload;
    }

    public void setReload(boolean reload)
    {
        this.reload = reload;
    }
}
