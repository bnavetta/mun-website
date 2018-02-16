package org.brownmun.db.committee;

import java.util.Objects;

import javax.persistence.*;

import com.google.common.base.MoreObjects;

import org.brownmun.db.school.Delegate;

/**
 * A position on a committee, such as the United States in the World Health
 * Organization.
 */
@Entity
public class Position
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of this position, usually a country or historical figure.
     */
    private String name;

    /**
     * The (possibly {@code null}) {@code Delegate} representing this position.
     */
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "position")
    private Delegate delegate;

    /**
     * The {@code Committee} this position is a part of.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "committee_id")
    private Committee committee;

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

    public Delegate getDelegate()
    {
        return delegate;
    }

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }

    public Committee getCommittee()
    {
        return committee;
    }

    public void setCommittee(Committee committee)
    {
        this.committee = committee;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Position position = (Position) o;
        return Objects.equals(name, position.name) && Objects.equals(committee, position.committee);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, committee);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("id", id).add("name", name).add("delegate", delegate)
                .add("committee", committee).toString();
    }
}
