package org.brownmun.web.support.webpack;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Representation of a Webpack manifest, mapping chunk names to
 * included files.
 */
public class Manifest
{
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final TypeReference<Map<String, List<String>>> MANIFEST_TYPE = new TypeReference<>() {};
    public static final Manifest EMPTY = new Manifest(Map.of());

    private final Map<String, List<String>> chunks;

    private Manifest(Map<String, List<String>> chunks)
    {
        this.chunks = chunks;
    }

    /**
     * Create a new {@link Manifest} from an input source
     * @param in the manifest contents
     * @return the manifest contents
     * @throws IOException if unable to parse the manifest JSON
     */
    public static Manifest load(InputStream in) throws IOException
    {
        return new Manifest(MAPPER.readValue(in, MANIFEST_TYPE));
    }

    /**
     * Get the files in a chunk
     * @param chunkName the name of the chunk
     * @return the names of files in the chunk, relative to the Webpack
     *     output directory
     * @throws IllegalArgumentException if the chunk doesn't exist
     */
    public List<String> getFiles(String chunkName)
    {
        List<String> files = chunks.get(chunkName);
        if (files != null)
        {
            return Collections.unmodifiableList(files);
        }

        throw new IllegalArgumentException("No such chunk: " + chunkName);
    }
}
