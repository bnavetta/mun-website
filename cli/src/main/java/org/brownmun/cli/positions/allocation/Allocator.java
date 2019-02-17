package org.brownmun.cli.positions.allocation;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.collect.Lists;

import org.brownmun.cli.positions.SchoolAllocation;
import org.brownmun.core.committee.model.*;
import org.brownmun.core.school.model.School;
import org.brownmun.core.school.model.School_;
import org.brownmun.core.school.model.SupplementalInfo;
import org.brownmun.core.school.model.SupplementalInfo_;

/**
 * Manages the whole process of allocating position types to schools.
 */
public class Allocator
{
    private static final Logger log = LoggerFactory.getLogger(Allocator.class);

    private final CsvMapper mapper;
    private final CsvSchema allocationSchema;

    // There are a couple weird one-off queries used here, so we use the
    // EntityManager API
    // directly instead of piping them through SchoolService
    private final EntityManager em;

    public Allocator(CsvMapper mapper, EntityManager em)
    {
        this.mapper = mapper;
        this.em = em;
        this.allocationSchema = mapper.schemaFor(SchoolAllocation.class).withHeader();
    }

    private List<SchoolAllocation> loadPreferences(File source) throws IOException
    {
        MappingIterator<SchoolAllocation> iter = mapper.readerFor(SchoolAllocation.class)
                .with(allocationSchema)
                .readValues(source);
        List<SchoolAllocation> alloc = iter.readAll();
        iter.close();
        return alloc;
    }

    private void writeAllocations(List<SchoolAllocation> allocations, File output) throws IOException
    {
        SequenceWriter out = mapper.writer(allocationSchema).writeValues(output);
        out.writeAll(allocations);
        out.close();
    }

    private long getMaxId()
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<School> root = query.from(School.class);
        query.select(cb.max(root.get(School_.id)));
        return em.createQuery(query).getSingleResult();
    }

    private long countPositions(CommitteeType type)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> count = cb.createQuery(Long.class);
        Root<Position> root = count.from(Position.class);
        Join<Position, Committee> committeeJoin = root.join(Position_.committee);
        count.select(cb.count(root));
        count.where(cb.equal(committeeJoin.get(Committee_.type), type));
        return em.createQuery(count).getSingleResult();
    }

    /**
     * Checks school preferences against the supplemental info submitted. This
     * verifies that we have preferences for all schools and that their preferences
     * match their intended delegation size.
     *
     * @return a list of errors in the preferences
     */
    private List<String> validatePreferences(List<SchoolAllocation> preferences)
    {
        List<String> errors = Lists.newArrayList();
        Map<Long, SchoolAllocation> prefMap = preferences.stream()
                .collect(Collectors.toMap(SchoolAllocation::id, Function.identity()));

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> countQuery = cb.createQuery(Tuple.class);
        Root<SupplementalInfo> root = countQuery.from(SupplementalInfo.class);
        countQuery.multiselect(root.get(SupplementalInfo_.id), root.get(SupplementalInfo_.delegateCount));
        for (Tuple t : em.createQuery(countQuery).getResultList())
        {
            long id = (Long) t.get(0);
            int delegateCount = (Integer) t.get(1);

            cb = em.getCriteriaBuilder();
            CriteriaQuery<Boolean> acceptedQ = cb.createQuery(Boolean.class);
            Root<School> r = acceptedQ.from(School.class);
            acceptedQ.select(r.get(School_.accepted)).where(cb.equal(r.get(School_.id), Long.valueOf(id)));
            if (!em.createQuery(acceptedQ).getSingleResult())
            {
                continue;
            }

            SchoolAllocation prefs = prefMap.get(id);
            if (prefs == null)
            {
                errors.add("Preferences missing for school " + id);
                continue;
            }

            if (prefs.totalDelegates() != delegateCount)
            {
                errors.add(String.format("Preferences for school %d don't add up: expected %d delegates, got %d", id,
                        delegateCount, prefs.totalDelegates()));
            }
        }

        return errors;
    }

    /**
     * Generate position type allocations for all schools.
     *
     * @param preferencesSource the file to read school preferences from
     * @param allocationDest the file to write allocations to
     * @param validate if true, check that the preferences are valid
     */
    public List<SchoolAllocation> generateAllocations(File preferencesSource, File allocationDest, boolean validate)
            throws IOException, IllegalArgumentException
    {
        log.info("Generating allocations from {} into {}", preferencesSource, allocationDest);
        List<SchoolAllocation> preferences = loadPreferences(preferencesSource);
        if (validate)
        {
            List<String> errors = validatePreferences(preferences);
            if (!errors.isEmpty())
            {
                throw new IllegalArgumentException("Invalid preferences: " + String.join("\n", errors));
            }
        }

        AllocationSolver solver = new AllocationSolver((int) getMaxId());

        long generalPositions = countPositions(CommitteeType.GENERAL);
        long specializedPositions = countPositions(CommitteeType.SPECIALIZED);
        // We model joint crisis committees as a separate type, but for the purposes of
        // allocation they're the same.
        long crisisPositions = countPositions(CommitteeType.CRISIS) + countPositions(CommitteeType.JOINT_CRISIS)
                + countPositions(CommitteeType.JOINT_CRISIS_ROOM);
        log.debug("Counted {} general positions, {} specialized positions, and {} crisis positions", generalPositions,
                specializedPositions, crisisPositions);

        List<SchoolAllocation> allocations = solver.allocate(Math.toIntExact(generalPositions),
                Math.toIntExact(specializedPositions), Math.toIntExact(crisisPositions), preferences);

        writeAllocations(allocations, allocationDest);
        return allocations;
    }
}
