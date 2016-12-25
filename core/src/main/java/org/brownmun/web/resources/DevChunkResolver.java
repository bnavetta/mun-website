package org.brownmun.web.resources;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;

/**
 * Reload from the built {@code asset-manifest.json} every time
 */
@Slf4j
@Service
@Profile("dev-assets")
public class DevChunkResolver extends AbstractChunkResolver
{
	// IntelliJ runs from the workspace root. Does Gradle?
	private final File statsJson = new File("frontend/dist/asset-manifest.json");

	@Override
	public String getAssetBase()
	{
		return "http://localhost:8000/";
	}

	@Override
	public Optional<Chunk> resolve(String chunkName)
	{
		log.debug("Loading stats.json at {}", statsJson.getAbsoluteFile());
		return loadChunks(statsJson.toURI()).map(chunks -> chunks.get(chunkName));
	}
}
