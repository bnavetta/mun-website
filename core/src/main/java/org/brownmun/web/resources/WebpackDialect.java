package org.brownmun.web.resources;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.Set;

@Component
public class WebpackDialect implements IProcessorDialect
{
	private final ChunkResolver chunkResolver;

	@Autowired
	public WebpackDialect(ChunkResolver chunkResolver)
	{
		this.chunkResolver = chunkResolver;
	}

	@Override
	public String getName()
	{
		return "webpack";
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
		return Sets.newHashSet(new ChunkElementProcessor(dialectPrefix, chunkResolver));
	}
}
