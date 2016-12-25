package org.brownmun.web.resources;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;

/**
 * Load the embedded {@code asset-manifest.json} once
 */
@Slf4j
@Service
@Profile("prod-assets")
public class ProdChunkResolver extends AbstractChunkResolver
{
	private Map<String, Chunk> chunks = ImmutableMap.of();

	@PostConstruct
	public void readManifest()
	{
		try
		{
			Optional<Map<String, Chunk>> loaded = loadChunks(getClass().getResource("/static/asset-manifest.json").toURI());
			if (loaded.isPresent())
			{
				this.chunks = loaded.get();
			}

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
