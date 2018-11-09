package org.brownmun.core.award.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

import com.google.common.base.MoreObjects;

/**
 * View over the {@link Award} table that includes information from other tables
 * for printing awards.
 */
@Entity
public class AwardPrint
{
    @Id
    private Long id;

    @Type(type = "org.brownmun.core.db.PostgresEnumType", parameters = @org.hibernate.annotations.Parameter(name = "postgres_enum", value = "award_type"))
    @Enumerated(EnumType.STRING)
    private AwardType type;

    private String committeeName;
    private String positionName;
    private String schoolName;
    private String delegateName;

    public Long getId()
    {
        return id;
    }

    public AwardType getType()
    {
        return type;
    }

    public String getCommitteeName()
    {
        return committeeName;
    }

    public String getPositionName()
    {
        return positionName;
    }

    public String getSchoolName()
    {
        return schoolName;
    }

    public String getDelegateName()
    {
        return delegateName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setType(AwardType type) {
        this.type = type;
    }

    public void setCommitteeName(String committeeName) {
        this.committeeName = committeeName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public void setDelegateName(String delegateName) {
        this.delegateName = delegateName;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("type", type)
                .add("committeeName", committeeName)
                .add("positionName", positionName)
                .add("schoolName", schoolName)
                .add("delegateName", delegateName)
                .toString();
    }
}
