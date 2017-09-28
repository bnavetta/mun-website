package org.brownmun.model.committee;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.collect.ComparisonChain;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * A MUN committee, like NATO
 */
@Entity
public class Committee implements Comparable<Committee>
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

//    @NotBlank
    private String description;

    @NotBlank
    private String shortName;

    private boolean jointCrisis;

    private String topic1;

    private String topic2;

    private String topic3;

    private String topic4;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CommitteeType committeeType;

    @OrderBy("name ASC")
    @OneToMany(mappedBy = "committee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Position> positions;

    @Formula("(SELECT COUNT(*) FROM position p WHERE p.committee_id = id AND p.delegate_id IS NOT NULL)")
    private int assignedPositions;

    @Formula("(SELECT COUNT(*) FROM position p WHERE p.committee_id = id)")
    private int totalPositions;

    @Transient
    public String getContactEmail()
    {
        // TODO: this will break for bucs
        return getShortName().toLowerCase() + "@busun.org";
    }

    @Transient
    public boolean hasTopics()
    {
        return !Strings.isNullOrEmpty(topic1) || !Strings.isNullOrEmpty(topic2)
            || !Strings.isNullOrEmpty(topic3) || !Strings.isNullOrEmpty(topic4);
    }

    @Override
    public int compareTo(Committee o)
    {
        return ComparisonChain.start()
            .compare(this.name, o.name)
            .compare(this.id, o.id)
            .result();
    }

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

    public boolean isJointCrisis()
    {
        return jointCrisis;
    }

    public void setJointCrisis(boolean jointCrisis)
    {
        this.jointCrisis = jointCrisis;
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

    public CommitteeType getCommitteeType()
    {
        return committeeType;
    }

    public void setCommitteeType(CommitteeType committeeType)
    {
        this.committeeType = committeeType;
    }

    public List<Position> getPositions()
    {
        return positions;
    }

    public void setPositions(List<Position> positions)
    {
        this.positions = positions;
    }

    public int getAssignedPositions()
    {
        return assignedPositions;
    }

    public void setAssignedPositions(int assignedPositions)
    {
        this.assignedPositions = assignedPositions;
    }

    public int getTotalPositions()
    {
        return totalPositions;
    }

    public void setTotalPositions(int totalPositions)
    {
        this.totalPositions = totalPositions;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("description", description)
                .add("shortName", shortName)
                .add("jointCrisis", jointCrisis)
                .add("topic1", topic1)
                .add("topic2", topic2)
                .add("topic3", topic3)
                .add("topic4", topic4)
                .add("committeeType", committeeType)
                .add("positions", positions)
                .add("assignedPositions", assignedPositions)
                .add("totalPositions", totalPositions)
                .toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Committee committee = (Committee) o;
        return jointCrisis == committee.jointCrisis &&
                assignedPositions == committee.assignedPositions &&
                totalPositions == committee.totalPositions &&
                Objects.equals(id, committee.id) &&
                Objects.equals(name, committee.name) &&
                Objects.equals(description, committee.description) &&
                Objects.equals(shortName, committee.shortName) &&
                Objects.equals(topic1, committee.topic1) &&
                Objects.equals(topic2, committee.topic2) &&
                Objects.equals(topic3, committee.topic3) &&
                Objects.equals(topic4, committee.topic4) &&
                committeeType == committee.committeeType;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, name, description, shortName, jointCrisis, topic1, topic2, topic3, topic4, committeeType, assignedPositions, totalPositions);
    }
}
