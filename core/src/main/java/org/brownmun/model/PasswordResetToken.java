package org.brownmun.model;

import lombok.Data;

import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Represents a request to reset an advisor's password.
 */
@Entity
@Data
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
}
