package org.brownmun.core.committee;

import java.util.List;
import java.util.OptionalLong;

import org.brownmun.core.committee.model.Committee;
import org.brownmun.core.committee.model.CommitteeType;

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
     * Get info about all committees.
     */
    CommitteeListing list();

    /**
     * Find the ID of the committee that a given position belongs to.
     * @param positionId the ID of the position
     * @return the committee ID, if found
     */
    OptionalLong findCommitteeId(long positionId);
}
