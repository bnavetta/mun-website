/**
 * Implements the algorithms for assigning committee positions to schools. This
 * happens in two phases:
 *
 * <ol>
 * <li>Allocation - determining how many positions in committees of each type a
 * school will have. This is implemented in
 * {@link org.brownmun.cli.assignment.Allocator}.</li>
 * <li>Assignment - given an allocation, assign specific positions to schools.
 * </li>
 * </ol>
 *
 * The breakdown is so that we can check the generated allocation for fairness
 * and special cases. For example, crisis committees are less common outside of
 * the US, so some international schools don't want any crisis spots.
 */
package org.brownmun.cli.assignment;
