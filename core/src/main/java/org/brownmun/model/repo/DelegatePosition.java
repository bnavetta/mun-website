package org.brownmun.model.repo;

public class DelegatePosition
{
    private long delegateId;
    private String delegateName;
    private String positionName;
    private String committeeName;

    public DelegatePosition()
    {
    }

    public DelegatePosition(long delegateId, String delegateName, String positionName, String committeeName)
    {
        this.delegateId = delegateId;
        this.delegateName = delegateName;
        this.positionName = positionName;
        this.committeeName = committeeName;
    }

    public long getDelegateId()
    {
        return delegateId;
    }

    public void setDelegateId(long delegateId)
    {
        this.delegateId = delegateId;
    }

    public String getDelegateName()
    {
        return delegateName;
    }

    public void setDelegateName(String delegateName)
    {
        this.delegateName = delegateName;
    }

    public String getPositionName()
    {
        return positionName;
    }

    public void setPositionName(String positionName)
    {
        this.positionName = positionName;
    }

    public String getCommitteeName()
    {
        return committeeName;
    }

    public void setCommitteeName(String committeeName)
    {
        this.committeeName = committeeName;
    }
}
