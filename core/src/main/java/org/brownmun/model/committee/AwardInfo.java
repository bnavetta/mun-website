package org.brownmun.model.committee;

public interface AwardInfo
{
    long getCommitteeId();
    String getCommitteeName();

    long getAwardId();
    AwardType getAwardType();

    long getPositionId();
    String getPositionName();

    String getDelegateName();

    long getSchoolId();
    String getSchoolName();
}
