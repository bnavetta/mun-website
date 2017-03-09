package org.brownmun.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Represents a delegate's attendance for their committee.
 */
@Entity
@Data
public class Attendance
{
    @Id
    @OneToOne
    @JoinColumn(name = "delegate_id", referencedColumnName = "id")
    private Delegate delegate;

    // TODO: link to staff member since they have no local state
    // is it needed?

    private boolean positionPaper;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "present", column = @Column(name = "session_one_present")),
        @AttributeOverride(name = "tally", column = @Column(name = "session_one_tally"))
    })
    private Session sessionOne;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "present", column = @Column(name = "session_two_present")),
        @AttributeOverride(name = "tally", column = @Column(name = "session_two_tally"))
    })
    private Session sessionTwo;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "present", column = @Column(name = "session_three_present")),
        @AttributeOverride(name = "tally", column = @Column(name = "session_three_tally"))
    })
    private Session sessionThree;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "present", column = @Column(name = "session_four_present")),
        @AttributeOverride(name = "tally", column = @Column(name = "session_four_tally"))
    })
    private Session sessionFour;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "present", column = @Column(name = "session_five_present")),
        @AttributeOverride(name = "tally", column = @Column(name = "session_five_tally"))
    })
    private Session sessionFive;

    @Data
    @Embeddable
    public static class Session
    {
        private boolean present;
        private int tally; // Tally of how many times the
    }
}
