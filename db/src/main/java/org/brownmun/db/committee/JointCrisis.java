package org.brownmun.db.committee;

import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

import com.google.common.base.MoreObjects;

/**
 * A Joint Crisis is a group of interrelated crisis committees, all interacting.
 *
 * For example, BUSUN XXI had an Arab Spring joint crisis, with committees for
 * the Egyptian protesters, the Egyptian military, and neighboring countries.
 */
@Entity
public class JointCrisis implements HasShortName
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the joint crisis.
     */
    private String name;

    /**
     * @see HasShortName
     */
    private String shortName;

    /**
     * A brief description of the crisis.
     */
    private String description;

    /**
     * The committees that are part of this crisis. Even though this is a
     * one-to-many relationship, we use a join table instead of a column on
     * {@link Committee} because most committees aren't part of a joint crisis.
     */
    @OneToMany
    @JoinTable(name = "joint_crisis_committees", joinColumns = @JoinColumn(name = "joint_crisis_id"), inverseJoinColumns = @JoinColumn(name = "committee_id"))
    private Set<Committee> committees;

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

    @Override
    public String getShortName()
    {
        return shortName;
    }

    public void setShortName(String shortName)
    {
        this.shortName = shortName;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Set<Committee> getCommittees()
    {
        return committees;
    }

    public void setCommittees(Set<Committee> committees)
    {
        this.committees = committees;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        JointCrisis that = (JointCrisis) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("id", id).add("name", name).add("shortName", shortName)
                .add("description", description).add("committees", committees).toString();
    }
}
