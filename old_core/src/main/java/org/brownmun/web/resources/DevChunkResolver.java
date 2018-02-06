package org.brownmun.web.resources;

import com.google.common.base.Suppliers;
import org.brownmun.ConferenceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@Service
@Profile("dev-assets")
public class DevChunkResolver extends AbstractChunkResolver
{
	private static final Logger log = LoggerFactory.getLogger(DevChunkResolver.class);

	// IntelliJ runs from the workspace root. Does Gradle?
	private final URI dllJson;
	private final URI statsJson = URI.create("http://localhost:8000/asset-manifest.json");

	private final Supplier<Map<String, Chunk>> chunks;

	@Autowired
	public DevChunkResolver(ConferenceProperties conference)
	{
		dllJson = new File("frontend/dist/" + conference.getName().toLowerCase() + "/development/dll.json").toURI();

		chunks = Suppliers.memoizeWithExpiration(
				() -> loadChunks(dllJson, statsJson),
				1,
				TimeUnit.SECONDS
		);
	}

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
