package org.brownmun.web.support.webpack;

import java.net.URI;
import java.util.Map;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.*;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

public class ChunkElementProcessor extends AbstractElementTagProcessor
{
    private final ChunkResolver resolver;

    ChunkElementProcessor(String dialectPrefix, ChunkResolver resolver)
    {
        super(TemplateMode.HTML, dialectPrefix, "chunk", true, null, false, 1000);
        this.resolver = resolver;
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
            IElementTagStructureHandler structureHandler)
    {
        String chunkName = tag.getAttributeValue("name");

        IModel model = context.getModelFactory().createModel();

        Chunk chunk = resolver.getChunk(chunkName);

        for (URI cssFile : chunk.css())
        {
            IStandaloneElementTag link = context.getModelFactory()
                    .createStandaloneElementTag("link", Map.of("rel", "stylesheet", "href", cssFile.toString()),
                            AttributeValueQuotes.DOUBLE, false, true);
            model.add(link);
        }

        for (URI jsFile : chunk.js())
        {
            IOpenElementTag scriptStart = context.getModelFactory()
                    .createOpenElementTag("script", Map.of("src", jsFile.toString(), "defer", "true"),
                            AttributeValueQuotes.DOUBLE, false);
            model.add(scriptStart);
            model.add(context.getModelFactory().createCloseElementTag("script"));
        }

        structureHandler.replaceWith(model, true);
    }
}
