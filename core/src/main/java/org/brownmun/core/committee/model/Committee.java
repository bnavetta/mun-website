package org.brownmun.core.committee.model;

import java.net.URI;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * A committee, such as the U.N. Security Council.
 */
@Entity
public class Committee
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the committee.
     */
    private String name;

    /**
     * Short names are human-readable identifiers for committees and joint crises.
     * They're used as the account names for email addresses and as filenames for
     * external content, like background guides.
     */
    private String shortName;

    /**
     * The type of committee this is
     */
    @Type(type = "org.brownmun.core.db.PostgresEnumType", parameters = @org.hibernate.annotations.Parameter(name = "postgres_enum", value = "committee_type"))
    @Enumerated(EnumType.STRING)
    private CommitteeType type;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "joint_crisis_rooms", joinColumns = @JoinColumn(name = "joint_crisis_id"), inverseJoinColumns = @JoinColumn(name = "room_id"))
    private Set<Committee> jointCrisisRooms;

    /**
     * A brief description of the committee.
     */
    private String description;

    /**
     * Topics for the committee. These are the major agenda items delegates will
     * address.
     */

    private String topic1;
    private String topic2;
    private String topic3;
    private String topic4;

    /**
     * The location to display the committee at on a map
     */

    private Double mapLatitude;
    private Double mapLongitude;

    /**
     * A banner image for this committee.
     */
    private URI image;

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

    public Double getMapLatitude()
    {
        return mapLatitude;
    }

    public void setMapLatitude(Double mapLatitude)
    {
        this.mapLatitude = mapLatitude;
    }

    public Double getMapLongitude()
    {
        return mapLongitude;
    }

    public void setMapLongitude(Double mapLongitude)
    {
        this.mapLongitude = mapLongitude;
    }

    public URI getImage()
    {
        return image;
    }

    public void setImage(URI image)
    {
        this.image = image;
    }

    public Set<Committee> getJointCrisisRooms()
    {
        Preconditions.checkState(type == CommitteeType.JOINT_CRISIS, "Not a joint crisis");
        return jointCrisisRooms;
    }

    public void setJointCrisisRooms(Set<Committee> jointCrisisRooms)
    {
        Preconditions.checkState(type == CommitteeType.JOINT_CRISIS, "Not a joint crisis");
        this.jointCrisisRooms = jointCrisisRooms;
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
        return MoreObjects.toStringHelper(this).add("id", id).add("name", name).add("shortName", shortName).toString();
    }
}
