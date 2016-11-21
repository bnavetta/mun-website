package org.brownmun.web.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Reload from the built {@code stats.json} every time
 */
@Slf4j
@Service
@Profile("local")
public class DevChunkResolver implements ChunkResolver
{
	private final ObjectMapper mapper;

	// IntelliJ runs from the workspace root. Does Gradle?
	private final File statsJson = new File("frontend/dist/stats.json");

	@Autowired
	public DevChunkResolver(ObjectMapper mapper)
	{
		this.mapper = mapper;
	}

	@Override
	public Optional<Collection<String>> resolve(String chunkName)
	{
		try
		{
			log.debug("Loading stats.json at {}", statsJson.getCanonicalFile());
			WebpackStats stats = mapper.readValue(statsJson, WebpackStats.class);
			return Optional.ofNullable(stats.getAssetsByChunkName().get(chunkName))
				.map(assets -> assets.stream().map(asset -> "http://localhost:8000/" + asset).collect(Collectors.toList()));
		}
		catch (IOException e)
		{
			log.warn("Error loading stats.json", e);
			return Optional.empty();
		}
	}
}
