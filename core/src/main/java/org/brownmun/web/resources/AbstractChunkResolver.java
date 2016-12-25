package org.brownmun.web.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URI;
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

	protected Optional<Map<String, Chunk>> loadChunks(URI source)
	{
		try
		{
			return Optional.of(mapper.readValue(source.toURL(), Chunk.CHUNK_MAP));
		}
		catch (IOException e)
		{
			log.warn("Error loading Webpack chunks from {}: {}", source, e);
			return Optional.empty();
		}
	}
}
