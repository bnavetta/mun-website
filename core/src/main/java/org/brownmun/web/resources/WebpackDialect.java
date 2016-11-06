package org.brownmun.web.resources;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.Set;

@Component
public class WebpackDialect extends AbstractDialect
{
	private final ChunkResolver chunkResolver;

	@Autowired
	public WebpackDialect(ChunkResolver chunkResolver)
	{
		this.chunkResolver = chunkResolver;
	}

	public String getPrefix()
	{
		return "wp";
	}

	@Override
	public Set<IProcessor> getProcessors()
	{
		return Sets.newHashSet(new ChunkElementProcessor(chunkResolver));
	}
}
