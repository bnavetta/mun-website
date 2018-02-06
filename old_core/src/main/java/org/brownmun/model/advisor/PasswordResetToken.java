package org.brownmun.model.advisor;

import com.google.common.base.MoreObjects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.Instant;

/**
 * Represents a request to reset an advisor's password.
 */
@Entity
public class PasswordResetToken
{
    /**
     * Code emailed to advisor for resetting their password.
     */
    @Id
    private String resetCode;

    /**
     * The advisor whose password is being reset.
     */
    @ManyToOne
    @JoinColumn(name = "advisor_id")
    private Advisor advisor;

    /**
     * When the password reset was requested, so we can see if it expired.
     */
    private Instant requestedAt;

    public String getResetCode()
    {
        return resetCode;
    }

    public void setResetCode(String resetCode)
    {
        this.resetCode = resetCode;
    }

    public Advisor getAdvisor()
    {
        return advisor;
    }

    public void setAdvisor(Advisor advisor)
    {
        this.advisor = advisor;
    }

    public Instant getRequestedAt()
    {
        return requestedAt;
    }

    public void setRequestedAt(Instant requestedAt)
    {
        this.requestedAt = requestedAt;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("resetCode", resetCode)
                .add("advisor", advisor)
                .add("requestedAt", requestedAt)
                .toString();
    }
}
