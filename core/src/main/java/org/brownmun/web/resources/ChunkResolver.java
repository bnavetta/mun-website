package org.brownmun.web.resources;

import java.util.Collection;
import java.util.Optional;

/**
 * Load a list of files contained in a particular Webpack chunk from {@code stats.json}
 */
public interface ChunkResolver
{
	Optional<Collection<String>> resolve(String chunkName);
}
