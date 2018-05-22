package org.brownmun.web.support.conference;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import org.brownmun.core.Conference;

public class IfConferenceAttributeProcessor extends AbstractAttributeTagProcessor
{
    private final Conference conference;
    private final String key;
    private final boolean show;

    public IfConferenceAttributeProcessor(String dialectPrefix, Conference conference, String key)
    {
        super(TemplateMode.HTML, dialectPrefix, null, false, "if-" + key, true, 1000, true);

        this.conference = conference;
        this.key = key;
        this.show = conference.getKey().equals(key);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
            String attributeValue, IElementTagStructureHandler structureHandler)
    {
        if (!show)
        {
            structureHandler.removeElement();
        }
    }
}
