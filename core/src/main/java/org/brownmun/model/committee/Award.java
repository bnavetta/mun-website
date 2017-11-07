package org.brownmun.model.committee;

import com.google.common.base.MoreObjects;

import javax.persistence.*;

@Entity
public class Award
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "position_id")
    private Position position;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "committee_id")
    private Committee committee;

    @Enumerated(EnumType.STRING)
    private AwardType awardType;

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

    public AwardType getAwardType()
    {
        return awardType;
    }

    public void setAwardType(AwardType type)
    {
        this.awardType = type;
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
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("committee", committee)
                .add("position", position)
                .add("awardType", awardType)
                .toString();
    }
}
