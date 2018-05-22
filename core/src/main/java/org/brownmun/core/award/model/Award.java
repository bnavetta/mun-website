package org.brownmun.core.award.model;

import javax.persistence.*;

import org.hibernate.annotations.Type;

import com.google.common.base.MoreObjects;

import org.brownmun.core.committee.model.Committee;
import org.brownmun.core.committee.model.Position;

/**
 * Records an award given to a position. Almost a join table with extra info
 * about what kind of award it is, but not quite because awards can be
 * unassigned.
 */
@Entity
public class Award
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "committee_id")
    private Committee committee;

    @Type(type = "org.brownmun.core.db.PostgresEnumType", parameters = @org.hibernate.annotations.Parameter(name = "postgres_enum", value = "award_type"))
    @Enumerated(EnumType.STRING)
    private AwardType type;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Position getPosition()
    {
        return position;
    }

    public void setPosition(Position position)
    {
        this.position = position;
    }

    public Committee getCommittee()
    {
        return committee;
    }

    public void setCommittee(Committee committee)
    {
        this.committee = committee;
    }

    public AwardType getType()
    {
        return type;
    }

    public void setType(AwardType type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("id", id).add("position", position).add("committee", committee)
                .add("type", type).toString();
    }
}
