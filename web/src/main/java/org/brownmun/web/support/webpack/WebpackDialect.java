package org.brownmun.web.support.webpack;

import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.Set;

public class WebpackDialect implements IProcessorDialect
{
    private final ChunkResolver resolver;

    public WebpackDialect(ChunkResolver loader)
    {
        this.resolver = loader;
    }

    @Override
    public String getPrefix()
    {
        return "wp";
    }

    @Override
    public int getDialectProcessorPrecedence()
    {
        return 1000;
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix)
    {
        return Set.of(new ChunkElementProcessor(dialectPrefix, resolver));
    }

    @Override
    public String getName()
    {
        return "webpack";
    }
}
