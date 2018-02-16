package org.brownmun.db.committee;

import java.util.Objects;

import javax.persistence.*;

import org.hibernate.annotations.Type;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;

/**
 * A committee, such as the U.N. Security Council.
 */
@Entity
public class Committee implements HasShortName
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the committee.
     */
    private String name;

    /**
     * A brief description of the committee.
     */
    private String description;

    /**
     * @see HasShortName
     */
    private String shortName;

    /**
     * The type of committee this is
     */
    @Type(type = "org.brownmun.db.support.PostgresEnumType", parameters = @org.hibernate.annotations.Parameter(name = "postgres_enum", value = "committee_type"))
    @Enumerated(EnumType.STRING)
    private CommitteeType type;

    /**
     * Topics for the committee. These are the major agenda items delegates will
     * address.
     */

    private String topic1;
    private String topic2;
    private String topic3;
    private String topic4;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override
    public String getShortName()
    {
        return shortName;
    }

    public void setShortName(String shortName)
    {
        this.shortName = shortName;
    }

    public String getTopic1()
    {
        return topic1;
    }

    public void setTopic1(String topic1)
    {
        this.topic1 = topic1;
    }

    public String getTopic2()
    {
        return topic2;
    }

    public void setTopic2(String topic2)
    {
        this.topic2 = topic2;
    }

    public String getTopic3()
    {
        return topic3;
    }

    public void setTopic3(String topic3)
    {
        this.topic3 = topic3;
    }

    public String getTopic4()
    {
        return topic4;
    }

    public void setTopic4(String topic4)
    {
        this.topic4 = topic4;
    }

    public CommitteeType getType()
    {
        return type;
    }

    public void setType(CommitteeType type)
    {
        this.type = type;
    }

    public boolean hasTopics()
    {
        return !Strings.isNullOrEmpty(topic1) || !Strings.isNullOrEmpty(topic2) || !Strings.isNullOrEmpty(topic3)
                || !Strings.isNullOrEmpty(topic4);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Committee committee = (Committee) o;
        return Objects.equals(name, committee.name);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("id", id).add("name", name).add("description", description)
                .add("shortName", shortName).add("type", type).add("topic1", topic1).add("topic2", topic2)
                .add("topic3", topic3).add("topic4", topic4).toString();
    }
}
