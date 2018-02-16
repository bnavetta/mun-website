package org.brownmun.web.support.webpack;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;

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
