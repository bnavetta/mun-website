package org.brownmun.core.committee;

import java.util.List;

import org.brownmun.db.committee.Committee;
import org.brownmun.db.committee.CommitteeType;
import org.brownmun.db.committee.JointCrisis;

/**
 * Responsible for committees.
 */
public interface CommitteeService
{
    /**
     * Save a committee to the database
     *
     * @param c the committee to save
     * @return the saved committee. This object should be used instead of the
     *         passed-in one. When saving a new committee, this will have the
     *         {@link Committee#getId()} field populated.
     */
    Committee save(Committee c);

    /**
     * Load all committees from the database
     *
     * @return a list of all the committees
     */
    List<Committee> all();

    /**
     * Fetch all committees of a type.
     *
     * @param type the type of committee to fetch
     * @return all committees of that type
     */
    List<Committee> allByType(CommitteeType type);

    /**
     * Fetch all joint crises.
     */
    List<JointCrisis> jointCrises();

    List<Committee> nonJointCrises();

    /**
     * Get info about all committees for display.
     */
    CommitteeListing displayListing();
}
