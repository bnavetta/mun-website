package org.brownmun.db.support;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.EnumType;
import org.postgresql.util.PGobject;

/**
 * Adapter for Postgres enum types
 */
public class PostgresEnumType extends EnumType
{
    private String enumType;

    @Override
    public void setParameterValues(Properties parameters)
    {
        super.setParameterValues(parameters);
        this.enumType = parameters.getProperty("postgres_enum");
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
            throws HibernateException, SQLException
    {
        if (value == null)
        {
            st.setNull(index, Types.OTHER);
        }
        else
        {
            PGobject obj = new PGobject();
            obj.setType(enumType);
            obj.setValue(value.toString());
            st.setObject(index, obj);
        }
    }
}
