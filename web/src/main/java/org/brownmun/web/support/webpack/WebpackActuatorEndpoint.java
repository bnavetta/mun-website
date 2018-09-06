package org.brownmun.web.support.webpack;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;

@Endpoint(id = "webpack")
public class WebpackActuatorEndpoint
{
    private final ChunkResolver chunkResolver;

    public WebpackActuatorEndpoint(ChunkResolver chunkResolver)
    {
        this.chunkResolver = chunkResolver;
    }

    @WriteOperation
    public void resetManifest()
    {
        chunkResolver.resetManifest();
    }

    @ReadOperation
    public Manifest getManifest()
    {
        return chunkResolver.getManifest();
    }
}
