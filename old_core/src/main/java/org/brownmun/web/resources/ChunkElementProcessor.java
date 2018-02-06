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

		chunkResolver.resolve(chunkName).ifPresent(chunk -> {
			for (String asset : chunk.getCss())
			{
				IStandaloneElementTag link = context.getModelFactory().createStandaloneElementTag("link", ImmutableMap.of(
					"rel", "stylesheet",
					"href", chunkResolver.getAssetBase() + asset
				), AttributeValueQuotes.DOUBLE, false, true);
				model.add(link);
			}

			for (String asset : chunk.getJs())
			{
				IOpenElementTag scriptStart = context.getModelFactory().createOpenElementTag("script", ImmutableMap.of(
					"src", chunkResolver.getAssetBase() + asset,
					// TODO: async or defer?
					"defer", "true"
				), AttributeValueQuotes.DOUBLE, false);
				model.add(scriptStart);
				model.add(context.getModelFactory().createCloseElementTag("script"));
			}
		});

		structureHandler.replaceWith(model, true);
	}
}
