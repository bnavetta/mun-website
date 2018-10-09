package org.brownmun.cli.positions.assignment;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.ortools.graph.MaxFlow;

import org.brownmun.cli.positions.SchoolAllocation;

/**
 * Determines how many positions in each committee each school should get.
 *
 * This is more specific (and after) allocation, because it gives schools
 * positions in committees while respecting their overall allocations by type.
 * In theory, we could get away with only this part, if we disregarded school
 * preferences about how many GA/crisis/etc. positions they wanted. It's tricky
 * to incorporate preferences into this positions step though, because the
 * optimization strategy is different. For allocation, we use a linear (integer)
 * programming solver, which takes a series of constraints and an objective
 * function. That works well for allocation, because deviation from the
 * preferences is a natural objective to minimize. For positions, we use a max
 * flow solver. Max flow is nice for this sort of problem because its notion of
 * capacities works well with committee and delegation sizes. Using capacities,
 * we can express things like a school having a certain number of delegates or a
 * committee only having so many delegates from a certain school.
 *
 * For max flow, we define a graph where edges have limited capacity, and the
 * solver shoves as much flow from the source node(s) to the destination(s). In
 * our case, we check that the flow equals the total number of delegates. If
 * not, we have to allow more delegates from the same school to be on the same
 * committee.
 *
 * The graph looks like this:
 *
 * <ul>
 * <li>We have one source node and one destination node</li>
 * <li>There's a node for each school connected to the source. The capacity of
 * this arc is the school's delegation size.</li>
 * <li>Each school has 3 extra nodes, one for general assemblies, one for
 * specialized/historical committees, and one for crisis and joint crisis
 * committees. There's an arc from the school node to each of these, and the
 * capacity of the arc is the number of positions of that type allocated to the
 * school.</li>
 * <li>Each committee has a node. There's an arc from each type-specific school
 * node of this committee's type to the committee node. The capacity of the arc
 * is the maximum number of students from the same school we're willing to have
 * in this committee.</li>
 * <li>All the committee nodes have an arc into the destination node with
 * capacity equal to the number of positions in the committee.</li>
 * </ul>
 */
public class AssignmentSolver
{
    private static final int SOURCE = 0;
    private static final int DESTINATION = 1;

    private static final Logger log = LoggerFactory.getLogger(AssignmentSolver.class);

    /**
     * Stores the arc index for each school-to-committee arc. The first index is the
     * school ID and the second is the committee ID. The arc index can be used to
     * look up the flow through that arc in the optimal solution, which is the
     * number of delegates from that school that should have positions on that
     * committee.
     *
     * Note: technically the source nodes for this are the type-specific school
     * nodes. For example, suppose the WHO committee's ID is 12. The entry at (99,
     * 12) will be from school 99's GA node to the WHO node.
     */
    private final int[][] committeeSpotArcs;

    private final int maxCommitteeId;
    private final int maxSchoolId;

    private final MaxFlow maxFlow;

    public AssignmentSolver(int maxSchoolId, int maxCommitteeId)
    {
        this.maxCommitteeId = maxCommitteeId;
        this.maxSchoolId = maxSchoolId;

        maxFlow = new MaxFlow();
        committeeSpotArcs = new int[maxSchoolId + 1][maxCommitteeId + 1];
    }

    private void buildGraph(AssignmentSettings settings, List<SchoolAllocation> allocations,
            List<AssignableCommittee> ga, List<AssignableCommittee> spec, List<AssignableCommittee> crisis)
    {
        int[] committeeNodes = new int[maxCommitteeId + 1];

        for (int id = 0; id <= maxCommitteeId; id++)
        {
            committeeNodes[id] = id + DESTINATION + 1;
        }

        // GA, spec, and crisis nodes are at schoolNodes[id] + 1, 2, and 3, respectively
        int[] schoolNodes = new int[maxSchoolId + 1];

        for (int id = 0; id <= maxSchoolId; id++)
        {
            schoolNodes[id] = maxCommitteeId + DESTINATION + 1 + 4 * id;
        }

        // Add school arcs
        for (SchoolAllocation school : allocations)
        {
            int schoolNode = schoolNodes[(int) school.id()];

            // Source -> school with capacity = delegation size
            maxFlow.addArcWithCapacity(SOURCE, schoolNode, school.totalDelegates());

            // school -> school's GA = GA allocation
            maxFlow.addArcWithCapacity(schoolNode, schoolNode + 1, school.general());

            // school -> school's spec = spec allocation
            maxFlow.addArcWithCapacity(schoolNode, schoolNode + 2, school.specialized());

            // school -> school's crisis = crisis allocation
            maxFlow.addArcWithCapacity(schoolNode, schoolNode + 3, school.crisis());

            // Now, we add the school-to-committee arcs

            for (AssignableCommittee committee : ga)
            {
                int arc = maxFlow.addArcWithCapacity(schoolNode + 1, committeeNodes[(int) committee.id()],
                        settings.generalOverlap());
                committeeSpotArcs[(int) school.id()][(int) committee.id()] = arc;
            }

            for (AssignableCommittee committee : spec)
            {
                int arc = maxFlow.addArcWithCapacity(schoolNode + 2, committeeNodes[(int) committee.id()],
                        settings.specializedOverlap());
                committeeSpotArcs[(int) school.id()][(int) committee.id()] = arc;
            }

            for (AssignableCommittee committee : crisis)
            {
                int arc = maxFlow.addArcWithCapacity(schoolNode + 3, committeeNodes[(int) committee.id()],
                        settings.crisisOverlap());
                committeeSpotArcs[(int) school.id()][(int) committee.id()] = arc;
            }
        }

        // Add committee-to-destination arcs
        for (AssignableCommittee committee : Iterables.concat(ga, spec, crisis))
        {
            maxFlow.addArcWithCapacity(committeeNodes[(int) committee.id()], DESTINATION, committee.capacity());
        }
    }

    /**
     * Look up the number of delegates from a certain school assigned to a
     * committee. Must be called after
     * {@link #solve(AssignmentSettings, List, List, List, List)}.
     */
    public int getDelegates(long schoolId, long committeeId)
    {
        Preconditions.checkElementIndex((int) schoolId, maxSchoolId + 1);
        Preconditions.checkElementIndex((int) committeeId, maxCommitteeId + 1);
        return Math.toIntExact(maxFlow.getFlow(committeeSpotArcs[(int) schoolId][(int) committeeId]));
    }

    /**
     * Use max flow to solve for how many delegates from each school should be in
     * each committee.
     *
     * @param settings configuration for the flow graph
     * @param allocations school allocations to committee types
     * @param ga all assignable GA committees
     * @param spec all assignable specialized committees
     * @param crisis all assignable crisis committees
     * @return {@code true} if there was an optimal solution using all delegates,
     *         {@code false} otherwise
     */
    public boolean solve(AssignmentSettings settings, List<SchoolAllocation> allocations, List<AssignableCommittee> ga,
            List<AssignableCommittee> spec, List<AssignableCommittee> crisis)
    {
        log.debug("Building flow graph");
        buildGraph(settings, allocations, ga, spec, crisis);
        log.debug("Solving for optimal flow");
        MaxFlow.Status status = maxFlow.solve(SOURCE, DESTINATION);
        if (status == MaxFlow.Status.OPTIMAL)
        {
            long flow = maxFlow.getOptimalFlow();
            long expectedFlow = allocations.stream().mapToInt(SchoolAllocation::totalDelegates).sum();
            log.debug("Maximum flow was {} (expected {})", flow, expectedFlow);
            return flow == expectedFlow;
        }
        else
        {
            log.debug("No optimal solution - status was {}", status);
            return false;
        }
    }
}
