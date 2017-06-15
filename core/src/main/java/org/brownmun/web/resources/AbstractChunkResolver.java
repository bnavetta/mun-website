package org.brownmun.web.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Slf4j
public abstract class AbstractChunkResolver implements ChunkResolver
{
	private ObjectMapper mapper;

	@Autowired
	public void setObjectMapper(ObjectMapper mapper)
	{
		this.mapper = mapper;
	}

	protected Optional<Map<String, Chunk>> loadManifest(URI source)
	{
		try
		{
			log.debug("Loading Webpack stats from {}", source);
			Map<String, Chunk> chunks = mapper.readValue(source.toURL(), Chunk.CHUNK_MAP);
			log.debug("Found chunks: {}", chunks);
			return Optional.of(chunks);
		}
		catch (IOException e)
		{
			log.warn("Error loading Webpack chunks from {}: {}", source, e);
			return Optional.empty();
		}
	}

	protected Map<String, Chunk> loadChunks(URI... sources)
	{
		return Arrays.stream(sources).reduce(
				ImmutableMap.<String, Chunk>of(),
				(chunks, source) -> loadManifest(source).map(
						newChunks -> ImmutableMap.<String, Chunk>builder().putAll(chunks).putAll(newChunks).build()
				).orElse(chunks),
				(a, b) -> ImmutableMap.<String, Chunk>builder().putAll(a).putAll(b).build()
		);
	}
}
