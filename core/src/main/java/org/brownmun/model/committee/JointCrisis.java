package org.brownmun.model.committee;

import com.google.common.base.MoreObjects;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.Collection;

/**
 * Crisis simulations can consist of multiple committees.
 */
@Entity
public class JointCrisis
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String shortName;

    private String description;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "joint_crisis_committees",
        joinColumns = @JoinColumn(name = "crisis_id"),
        inverseJoinColumns = @JoinColumn(name = "committee_id")
    )
    private Collection<Committee> committees;

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

    public Collection<Committee> getCommittees()
    {
        return committees;
    }

    public void setCommittees(Collection<Committee> committees)
    {
        this.committees = committees;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("description", description)
                .add("committees", committees)
                .toString();
    }
}
