package org.brownmun.core.db;

import com.google.common.base.Strings;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.net.URI;

@Converter(autoApply = true)
public class URIConverter implements AttributeConverter<URI, String>
{
    @Override
    public String convertToDatabaseColumn(URI attribute)
    {
        return attribute == null ? null : attribute.toString();
    }

    @Override
    public URI convertToEntityAttribute(String dbData)
    {
        return Strings.isNullOrEmpty(dbData) ? null : URI.create(dbData.trim());
    }
}
