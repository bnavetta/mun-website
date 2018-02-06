package org.brownmun.model;

import javax.persistence.AttributeConverter;
import java.util.BitSet;

public class BitSetAttributeConverter implements AttributeConverter<BitSet, byte[]>
{
    @Override
    public byte[] convertToDatabaseColumn(BitSet attribute)
    {
        return attribute.toByteArray();
    }

    @Override
    public BitSet convertToEntityAttribute(byte[] dbData)
    {
        return BitSet.valueOf(dbData);
    }
}
