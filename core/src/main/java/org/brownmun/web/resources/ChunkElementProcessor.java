package org.brownmun.web.resources;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.processor.element.AbstractMarkupSubstitutionElementProcessor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Element processor for adding Webpack chunk assets
 */
public class ChunkElementProcessor extends AbstractMarkupSubstitutionElementProcessor
{
	private final ChunkResolver chunkResolver;

	public ChunkElementProcessor(ChunkResolver chunkResolver)
	{
		super("chunk");
		this.chunkResolver = chunkResolver;
	}

	@Override
	protected List<Node> getMarkupSubstitutes(Arguments arguments, Element element)
	{
		String chunkName = element.getAttributeValue("name");
		return chunkResolver.resolve(chunkName).map((assets) -> assets.stream().map((asset) -> {
			if (asset.endsWith(".css"))
			{
				final Element link = new Element("link");
				link.setAttribute("rel", "stylesheet");
				link.setAttribute("href", "/" + asset);
				return link;
			}
			else if (asset.endsWith(".js"))
			{
				final Element script = new Element("script");
				script.setAttribute("src", "/" + asset);
				return script;
			} else
			{
				throw new IllegalArgumentException("Unsupported asset type: " + asset);
			}
		}).collect(Collectors.<Node>toList())).orElse(Collections.emptyList());
	}

	@Override
	public int getPrecedence()
	{
		return 1000;
	}
}
