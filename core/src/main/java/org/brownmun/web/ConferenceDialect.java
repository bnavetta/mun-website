package org.brownmun.web;

import com.google.common.collect.ImmutableSet;
import org.brownmun.ConferenceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.Set;

@Component
public class ConferenceDialect implements IExpressionObjectDialect
{
    private final ConferenceExpressionObjectFactory expressionObjectFactory;

    @Autowired
    public ConferenceDialect(ConferenceProperties properties)
    {
        this.expressionObjectFactory = new ConferenceExpressionObjectFactory(properties);
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory()
    {
        return expressionObjectFactory;
    }

    @Override
    public String getName()
    {
        return "conference";
    }

    private static final class ConferenceExpressionObjectFactory implements IExpressionObjectFactory
    {
        private static final String CONFERENCE_EXPRESSION_OBJECT_NAME = "conference";
        private static final Set<String> ALL_EXPRESSION_OBJECT_NAMES = ImmutableSet.of(CONFERENCE_EXPRESSION_OBJECT_NAME);

        private final ConferenceProperties conferenceProperties;

        public ConferenceExpressionObjectFactory(ConferenceProperties properties)
        {
            this.conferenceProperties = properties;
        }

        @Override
        public Set<String> getAllExpressionObjectNames()
        {
            return ALL_EXPRESSION_OBJECT_NAMES;
        }

        @Override
        public Object buildObject(IExpressionContext context, String expressionObjectName)
        {
            if (CONFERENCE_EXPRESSION_OBJECT_NAME.equals(expressionObjectName))
            {
                return conferenceProperties;
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
