package org.brownmun.web.support.webpack;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ChunkResolver
{
    private static final Logger log = LoggerFactory.getLogger(ChunkResolver.class);

    private final URI assetBase;
    private final Supplier<Manifest> manifestSource;

    public ChunkResolver(URI assetBase, boolean reload)
    {
        log.debug("Using Webpack assets from {}", assetBase);
        this.assetBase = assetBase;

        if (reload)
        {
            this.manifestSource = this::loadManifest;
        }
        else
        {
            Manifest manifest = loadManifest();
            this.manifestSource = () -> manifest;
        }
    }

    public Chunk getChunk(String name)
    {
        List<URI> files = manifestSource.get().getFiles(name)
                .stream()
                .map(assetBase::resolve)
                .collect(Collectors.toList());

        List<URI> js = Lists.newArrayList();
        List<URI> css = Lists.newArrayList();
        for (URI asset : files)
        {
            if (asset.getPath().endsWith(".js"))
            {
                js.add(asset);
            }
            else if (asset.getPath().endsWith(".css"))
            {
                css.add(asset);
            }
            else
            {
                log.warn("Unknown asset type: {}", asset);
            }
        }

        return Chunk.create(css, js);
    }

    private Manifest loadManifest()
    {
        try
        {
            URL manifestUrl = assetBase.resolve("manifest.json").toURL();
            log.debug("Loading Webpack manifest from {}", manifestUrl);
            try (InputStream in = manifestUrl.openStream())
            {
                return Manifest.load(in);
            }
        }
        catch (IOException e)
        {
            log.error("Error loading Webpack manifest", e);
            return Manifest.EMPTY;
        }
    }
}
