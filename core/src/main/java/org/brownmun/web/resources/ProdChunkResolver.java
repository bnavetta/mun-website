package org.brownmun.web.resources;

import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;

/**
 * Load the embedded {@code asset-manifest.json} once
 */
@Service
@Profile("prod-assets")
public class ProdChunkResolver extends AbstractChunkResolver
{
	private static final Logger log = LoggerFactory.getLogger(ProdChunkResolver.class);
	
	private Map<String, Chunk> chunks = ImmutableMap.of();

	@PostConstruct
	public void readManifest()
	{
		try
		{
			URI assetManifest = getClass().getResource("/static/asset-manifest.json").toURI();
			URI dllManifest = getClass().getResource("/static/dll.json").toURI();

			this.chunks = loadChunks(dllManifest, assetManifest);
		}
		catch (URISyntaxException e)
		{
			log.warn("Error loading manifest", e);
		}
	}

	@Override
	public String getAssetBase()
	{
		return "/";
	}

	@Override
	public Optional<Chunk> resolve(String chunkName)
	{
		return Optional.ofNullable(chunks.get(chunkName));
	}
}
