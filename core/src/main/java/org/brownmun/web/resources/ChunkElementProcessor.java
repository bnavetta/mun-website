package org.brownmun.web.resources;

import com.google.common.collect.ImmutableMap;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.*;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * Element processor for adding Webpack chunk assets
 */
public class ChunkElementProcessor extends AbstractElementTagProcessor
{
	private final ChunkResolver chunkResolver;

	public ChunkElementProcessor(String dialectPrefix, ChunkResolver chunkResolver)
	{
		super(TemplateMode.HTML, dialectPrefix, "chunk", true, null, false, 1000);
		this.chunkResolver = chunkResolver;
	}

	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag, IElementTagStructureHandler structureHandler)
	{
		String chunkName = tag.getAttributeValue("name");

		IModel model = context.getModelFactory().createModel();

		chunkResolver.resolve(chunkName).ifPresent((assets) -> {
			for (String asset : assets) {
				if (asset.endsWith(".css"))
				{
					IStandaloneElementTag link = context.getModelFactory().createStandaloneElementTag("link", ImmutableMap.of(
						"rel", "stylesheet",
						"href", asset
					), AttributeValueQuotes.DOUBLE, false, true);
					model.add(link);
				}
				else if (asset.endsWith(".js"))
				{
					IOpenElementTag scriptStart = context.getModelFactory().createOpenElementTag("script", "src", asset);
					model.add(scriptStart);
					model.add(context.getModelFactory().createCloseElementTag("script"));
				}
				else
				{
					throw new IllegalArgumentException("Unsupported asset type: " + asset);
				}
			}
		});

		structureHandler.replaceWith(model, true);
	}
}
