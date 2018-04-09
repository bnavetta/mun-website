package org.brownmun.core.school.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.Instant;

/**
 * A password reset request
 */
@Entity
public class PasswordReset
{
    /**
     * Code emailed to the advisor for resetting their password.
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
}
