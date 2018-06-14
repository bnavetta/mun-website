package org.brownmun.cli.positions.allocation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import org.brownmun.cli.positions.SchoolAllocation;

/**
 * System for determining how many positions in each committee type each school
 * gets. This attempts to match school preferences as closely as possible while
 * respecting constraints such as some international schools not being familiar
 * with crisis procedure or others not being in the midnight crisis committee.
 */
public class AllocationSolver
{
    private static final Logger log = LoggerFactory.getLogger(AllocationSolver.class);

    static final int GENERAL = 0;
    static final int SPECIALIZED = 1;
    static final int CRISIS = 2;
    private static final int NUM_TYPES = 3;
    private static final String[] TYPE_NAMES = new String[] { "general", "specialized", "crisis" };

    static
    {
        // TODO: put this somewhere else
        System.loadLibrary("jniortools");
    }

    /**
     * A matrix of solver variables representing how many positions of a given type
     * to give to a particular school. The first index is the school ID and the
     * second is the ordinal value of the committee type.
     */
    private final MPVariable[][] allocationVars;

    private final MPSolver solver;

    public AllocationSolver(int maxSchoolId)
    {
        solver = new MPSolver("CommitteeTypeAllocation", MPSolver.OptimizationProblemType.BOP_INTEGER_PROGRAMMING);

        allocationVars = new MPVariable[maxSchoolId + 1][NUM_TYPES];
        for (int id = 0; id <= maxSchoolId; id++)
        {
            for (int type = 0; type < NUM_TYPES; type++)
            {
                allocationVars[id][type] = solver.makeIntVar(0, 1000, "alloc_" + id + "_" + TYPE_NAMES[type]);
            }
        }
    }

    /**
     * Add constraints so that we do not allocate more positions than we have for
     * each committee type.
     */
    private void applyPositionLimits(int general, int specialized, int crisis)
    {
        // lower bound is 0, upper bound is # of positions
        MPConstraint genConstraint = solver.makeConstraint(0, general, "total_general_positions");
        for (MPVariable[] allocationVar : allocationVars)
        {
            genConstraint.setCoefficient(allocationVar[GENERAL], 1);
        }

        MPConstraint specConstraint = solver.makeConstraint(0, specialized, "total_specialized_positions");
        for (MPVariable[] allocationVar : allocationVars)
        {
            specConstraint.setCoefficient(allocationVar[SPECIALIZED], 1);
        }

        MPConstraint crisisConstraint = solver.makeConstraint(0, crisis, "total_crisis_positions");
        for (MPVariable[] allocationVar : allocationVars)
        {
            crisisConstraint.setCoefficient(allocationVar[CRISIS], 1);
        }
    }

    /**
     * Adds constraints so that each school is allocated enough positions for all
     * its delegates.
     */
    private void applyDelegateTotals(List<SchoolAllocation> preferences)
    {
        for (SchoolAllocation prefs : preferences)
        {
            int id = (int) prefs.id();
            MPConstraint constraint = solver.makeConstraint(prefs.totalDelegates(), prefs.totalDelegates(),
                    "school_" + id + "_total_delegates");
            for (int type = 0; type < NUM_TYPES; type++)
            {
                constraint.setCoefficient(allocationVars[id][type], 1);
            }
        }
    }

    /**
     * Create variables constrained to be the difference between requested and
     * actual committee allocations.
     *
     * @param preferences the school preferences
     * @return the error variables, which will be minimized as the objective
     *         function
     */
    private List<MPVariable> createErrorVariables(List<SchoolAllocation> preferences)
    {
        // See https://math.stackexchange.com/a/623569 for the general idea
        // Basically, we create extra variables and constrain them to be the absolute
        // value of the difference
        // between the requested and actual values.

        List<MPVariable> errors = Lists.newArrayList();
        for (SchoolAllocation prefs : preferences)
        {
            int id = (int) prefs.id();
            int[] requested = new int[] { prefs.general(), prefs.specialized(), prefs.crisis() };

            for (int type = 0; type < NUM_TYPES; type++)
            {
                MPVariable error = solver.makeIntVar(-1000, 1000, "error_" + id + "_" + TYPE_NAMES[type]);
                errors.add(error);

                // This expresses the constraint error >= actual - requested
                // Which can be written as requested >= actual - error
                MPConstraint pos = solver.makeConstraint(-MPSolver.infinity(), requested[type],
                        "error_pos_" + TYPE_NAMES[type] + "_" + id);
                pos.setCoefficient(allocationVars[id][type], 1);
                pos.setCoefficient(error, -1);

                // And this expresses error >= -(actual - requested)
                // Which is -requested >= -actual - error
                MPConstraint neg = solver.makeConstraint(-MPSolver.infinity(), -requested[type],
                        "error_neg_" + TYPE_NAMES[type] + "_" + id);
                neg.setCoefficient(allocationVars[id][type], -1);
                neg.setCoefficient(error, -1);
            }
        }
        return errors;
    }

    /**
     * Add constraints for "prohibitions". These are cases where we can't give a
     * school <strong>any</strong> positions of a certain type, most commonly
     * crisis. Schools indicate this by putting a {@code 0} as their requested
     * amount for that type.
     */
    private void applyProhibitions(List<SchoolAllocation> preferences)
    {
        for (SchoolAllocation prefs : preferences)
        {
            if (prefs.general() == 0)
            {
                log.debug("School {} will not be assigned any GA positions", prefs.id());
                MPConstraint constraint = solver.makeConstraint(0, 0, "prohibit_general_" + prefs.id());
                constraint.setCoefficient(allocationVars[(int) prefs.id()][GENERAL], 1);
            }

            if (prefs.specialized() == 0)
            {
                log.debug("School {} will not be assigned any specialized positions", prefs.id());
                MPConstraint constraint = solver.makeConstraint(0, 0, "prohibit_specialized_" + prefs.id());
                constraint.setCoefficient(allocationVars[(int) prefs.id()][SPECIALIZED], 1);
            }

            if (prefs.crisis() == 0)
            {
                log.debug("School {} will not be assigned any crisis positions", prefs.id());
                MPConstraint constraint = solver.makeConstraint(0, 0, "prohibit_crisis_" + prefs.id());
                constraint.setCoefficient(allocationVars[(int) prefs.id()][CRISIS], 1);
            }
        }
    }

    public List<SchoolAllocation> allocate(int totalGeneral, int totalSpecialized, int totalCrisis,
            List<SchoolAllocation> preferences)
    {
        int totalRequested = 0;
        for (SchoolAllocation prefs : preferences)
        {
            totalRequested += prefs.totalDelegates();
        }
        if (totalRequested > totalGeneral + totalSpecialized + totalCrisis)
        {
            throw new IllegalArgumentException("Not enough positions for " + totalRequested + " delegates");
        }

        log.info("Adding constraints ({} general, {} specialized, {} crisis)", totalGeneral, totalSpecialized,
                totalCrisis);
        applyPositionLimits(totalGeneral, totalSpecialized, totalCrisis);
        applyDelegateTotals(preferences);
        applyProhibitions(preferences);
        List<MPVariable> errors = createErrorVariables(preferences);

        log.info("Defining objective function over {} schools", preferences.size());
        MPObjective objective = solver.objective();
        for (MPVariable error : errors)
        {
            objective.setCoefficient(error, 1);
        }
        objective.setMinimization();

        String model = solver.exportModelAsLpFormat(false);
        log.debug(model);

        log.info("Solving allocation problem");
        MPSolver.ResultStatus result = solver.solve();
        log.info("Solver result status: " + result);
        if (result == MPSolver.ResultStatus.OPTIMAL)
        {
            if (!solver.verifySolution(1e-7, true))
            {
                throw new IllegalStateException("Solver solution violated problem constraints by more than 1e-7");
            }

            log.debug("Objective value (sum of error variables): {}", solver.objective().value());

            List<SchoolAllocation> allocation = Lists.newArrayListWithExpectedSize(allocationVars.length);
            for (SchoolAllocation prefs : preferences)
            {
                int id = (int) prefs.id();
                int general = (int) allocationVars[id][GENERAL].solutionValue();
                int specialized = (int) allocationVars[id][SPECIALIZED].solutionValue();
                int crisis = (int) allocationVars[id][CRISIS].solutionValue();
                allocation.add(SchoolAllocation.create(id, general, specialized, crisis));
            }

            return allocation;
        }
        else
        {
            throw new IllegalStateException("Unable to solve integer program: " + result);
        }
    }
}
