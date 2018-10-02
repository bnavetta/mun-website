package org.brownmun.core.committee;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.Stream;

import org.brownmun.core.committee.model.Committee;
import org.brownmun.core.committee.model.CommitteeType;
import org.brownmun.core.committee.model.Position;
import org.brownmun.core.school.model.Delegate;

/**
 * Responsible for committees.
 */
public interface CommitteeService
{
    /**
     * Find the committee with the given ID
     */
    Optional<Committee> getCommittee(long id);

    /**
     * Find the committee with the given short name.
     */
    Optional<Committee> findByShortName(String shortName);

    /**
     * Find the position with the given ID
     */
    Optional<Position> getPosition(long id);

    /**
     * Get the full name of a committee, including any joint crisis information.
     */
    String getFullName(Committee c);

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
     * Save a position to the database. The position <em>must</em> be assigned to a
     * committee, but need not be assigned a delegate.
     */
    Position savePosition(Position position);

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
    Stream<Committee> allByType(CommitteeType type);

    /**
     * Get info about all committees.
     */
    CommitteeListing list();

    /**
     * Find the ID of the committee that a given position belongs to.
     *
     * @param positionId the ID of the position
     * @return the committee ID, if found
     */
    OptionalLong findCommitteeId(long positionId);

    /**
     * Gets all positions on a committee.
     */
    Collection<Position> getPositions(Committee c);

    /**
     * Assign a position to a school. If the position is already assigned, it will
     * be unassigned from its current delegate. A new delegate will be created if
     * needed.
     *
     * @return the {@link Delegate} representing the assignment.
     */
    Delegate assignPosition(long positionId, long schoolId);
}
