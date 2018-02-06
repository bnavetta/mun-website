package org.brownmun.web.support.conference;

import org.brownmun.core.Conference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.processor.IProcessor;

import java.util.Set;

/**
 * Thymeleaf dialect for conference info
 */
@Service
public class ConferenceDialect implements IExpressionObjectDialect, IProcessorDialect
{
    private final Conference conference;

    @Override
    public String getName()
    {
        return "conference";
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory()
    {
        return new ConferenceExpressionObjectFactory();
    }

    @Autowired
    public ConferenceDialect(Conference conference)
    {
        this.conference = conference;
    }

    @Override
    public String getPrefix()
    {
        return "conference";
    }

    @Override
    public int getDialectProcessorPrecedence()
    {
        return 1000;
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix)
    {
        return Set.of(
                new IfConferenceAttributeProcessor(dialectPrefix, conference, "busun"),
                new IfConferenceAttributeProcessor(dialectPrefix, conference, "bucs")
        );
    }

    private class ConferenceExpressionObjectFactory implements IExpressionObjectFactory
    {
        @Override
        public Set<String> getAllExpressionObjectNames()
        {
            return Set.of("conference");
        }

        @Override
        public Object buildObject(IExpressionContext context, String expressionObjectName)
        {
            if ("conference".equals(expressionObjectName))
            {
                return conference;
            }

            return null;
        }

        @Override
        public boolean isCacheable(String expressionObjectName)
        {
            return true;
        }
    }
}
