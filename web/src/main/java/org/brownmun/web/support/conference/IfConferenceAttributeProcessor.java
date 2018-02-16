package org.brownmun.web.support.conference;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.standard.processor.AbstractStandardConditionalVisibilityTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import org.brownmun.core.Conference;

public class IfConferenceAttributeProcessor extends AbstractStandardConditionalVisibilityTagProcessor
{
    private final Conference conference;
    private final String key;

    public IfConferenceAttributeProcessor(String dialectPrefix, Conference conference, String key)
    {
        super(TemplateMode.HTML, dialectPrefix, "if-" + key, 1000);
        this.conference = conference;
        this.key = key;
    }

    @Override
    protected boolean isVisible(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
            String attributeValue)
    {
        return key.equals(conference.getKey());
    }
}
