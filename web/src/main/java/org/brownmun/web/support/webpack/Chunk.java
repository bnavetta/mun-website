package org.brownmun.web.support.webpack;

import com.google.auto.value.AutoValue;

import java.net.URI;
import java.util.List;

@AutoValue
public abstract class Chunk
{
    static Chunk create(List<URI> css, List<URI> js)
    {
        return new AutoValue_Chunk(css, js);
    }

    abstract List<URI> css();
    abstract List<URI> js();
}
