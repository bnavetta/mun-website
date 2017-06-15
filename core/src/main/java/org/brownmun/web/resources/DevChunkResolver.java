package org.brownmun.web.resources;

import com.google.common.base.Suppliers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Reload from the built {@code asset-manifest.json} every time
 */
@Slf4j
@Service
@Profile("dev-assets")
public class DevChunkResolver extends AbstractChunkResolver
{
	// IntelliJ runs from the workspace root. Does Gradle?
	private final URI dllJson = new File("frontend/dist/development/dll.json").toURI();
	private final URI statsJson = URI.create("http://localhost:8000/asset-manifest.json");

	private Supplier<Map<String, Chunk>> chunks = Suppliers.memoizeWithExpiration(
			() -> loadChunks(dllJson, statsJson),
			1,
			TimeUnit.SECONDS
	);

	@Override
	public String getAssetBase()
	{
		return "http://localhost:8000/";
	}

	@Override
	public Optional<Chunk> resolve(String chunkName)
	{
		return Optional.ofNullable(chunks.get().get(chunkName));
	}
}
