package org.brownmun.cli.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.collect.Maps;

import org.brownmun.cli.positions.SchoolAllocation;
import org.brownmun.cli.positions.allocation.Allocator;
import org.brownmun.cli.positions.assignment.Assigner;
import org.brownmun.cli.positions.assignment.PositionAssignment;
import org.brownmun.core.committee.CommitteeService;
import org.brownmun.core.committee.model.Position;
import org.brownmun.core.school.SchoolService;
import org.brownmun.core.school.model.Delegate;
import org.brownmun.core.school.model.School;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciithemes.a8.A8_Grids;

@ShellComponent
public class AssignmentCommands
{
    static
    {
        // TODO: put this somewhere else
        System.loadLibrary("jniortools");
    }

    private final SchoolService schoolService;
    private final CommitteeService committeeService;
    private final Allocator allocator;
    private final Assigner assigner;
    private final CsvMapper csvMapper;

    @Autowired
    public AssignmentCommands(SchoolService schoolService, CommitteeService committeeService, Allocator allocator,
            Assigner assigner)
    {
        this.schoolService = schoolService;
        this.committeeService = committeeService;
        this.allocator = allocator;
        this.assigner = assigner;
        this.csvMapper = new CsvMapper();
    }

    @ShellMethod("Generate committee type allocations")
    public String generateAllocations(File preferences, File output, boolean validate) throws IOException
    {
        List<SchoolAllocation> allocations = allocator.generateAllocations(preferences, output, validate);

        AsciiTable table = new AsciiTable();
        table.getContext().setGrid(A8_Grids.lineDoubleBlocks());
        table.addRow("School ID", "School Name", "General", "Specialized", "Crisis");
        table.addStrongRule();
        for (SchoolAllocation allocation : allocations)
        {
            School school = schoolService.getSchool(allocation.id())
                    .orElseThrow(() -> new IllegalArgumentException("No school with ID " + allocation.id()));
            table.addRow(allocation.id(), school.getName(), allocation.general(), allocation.specialized(),
                    allocation.crisis());
            table.addRule();
        }

        return table.render();
    }

    @ShellMethod("Check allocations against preferences")
    public AttributedString checkAllocations(File preferencesFile, File allocationsFile) throws IOException
    {
        AttributedStringBuilder result = new AttributedStringBuilder();

        ObjectReader r = csvMapper.readerFor(SchoolAllocation.class)
                .with(csvMapper.schemaFor(SchoolAllocation.class).withHeader());
        try (MappingIterator<SchoolAllocation> prefIter = r.readValues(preferencesFile);
                MappingIterator<SchoolAllocation> allocIter = r.readValues(allocationsFile))
        {
            List<SchoolAllocation> prefs = prefIter.readAll();
            Map<Long, SchoolAllocation> allocations = Maps.newHashMap();
            allocIter.forEachRemaining(a -> allocations.put(a.id(), a));

            // TODO: code cleanup

            for (SchoolAllocation pref : prefs)
            {
                SchoolAllocation alloc = allocations.get(pref.id());

                School school = schoolService.getSchool(pref.id()).get();
                result.append(String.format("%30s (%d): ", school.getName(), school.getId()));

                int gaDelta = alloc.general() - pref.general();
                AttributedStyle gaStyle;
                if (gaDelta >= 0)
                {
                    gaStyle = AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN);
                }
                else
                {
                    gaStyle = AttributedStyle.DEFAULT.foreground(AttributedStyle.RED);
                }
                result.styled(gaStyle, String.format("%+d ", gaDelta));

                int specDelta = alloc.specialized() - pref.specialized();
                AttributedStyle specStyle;
                if (specDelta >= 0)
                {
                    specStyle = AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN);
                }
                else
                {
                    specStyle = AttributedStyle.DEFAULT.foreground(AttributedStyle.RED);
                }
                result.styled(specStyle, String.format("%+d ", specDelta));

                int crisisDelta = alloc.crisis() - pref.crisis();
                AttributedStyle crisisStyle;
                if (crisisDelta >= 0)
                {
                    crisisStyle = AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN);
                }
                else
                {
                    crisisStyle = AttributedStyle.DEFAULT.foreground(AttributedStyle.RED);
                }
                result.styled(crisisStyle, String.format("%+d\n", crisisDelta));
            }
        }
        catch (IOException e)
        {
            throw e;
        }

        return result.toAttributedString();
    }

    @ShellMethod("Assign positions to schools")
    public String generateAssignments(File settings, File allocations, File output) throws IOException
    {
        List<PositionAssignment> assignments = assigner.assign(settings, allocations, output);

        AsciiTable table = new AsciiTable();
        table.getContext().setGrid(A8_Grids.lineDoubleBlocks());
        table.addRow("School", "Committee", "Position");
        table.addStrongRule();
        for (PositionAssignment assignment : assignments)
        {
            table.addRow(assignment.schoolName(), assignment.committeeName(), assignment.positionName());
            table.addRule();
        }

        return table.render();
    }

    @ShellMethod("Apply position assignments")
    public String applyAssignments(File assignmentsFile) throws IOException
    {
        CsvSchema assignmentSchema = csvMapper.typedSchemaFor(PositionAssignment.class).withHeader();

        List<PositionAssignment> assignments;
        try (MappingIterator<PositionAssignment> iter = csvMapper.readerFor(PositionAssignment.class)
                .with(assignmentSchema)
                .readValues(assignmentsFile))
        {
            assignments = iter.readAll();
        }

        // First, delete all delegates so we don't have to worry about merging in
        // existing assignments.
        schoolService.clearDelegates();

        System.out.println("Assigning positions");

        for (PositionAssignment assignment : assignments)
        {
            School school = schoolService.getSchool(assignment.schoolId())
                    .orElseThrow(() -> new RuntimeException("No school with ID " + assignment.schoolId()));

            Position position = committeeService.getPosition(assignment.positionId())
                    .orElseThrow(() -> new RuntimeException("No position with ID " + assignment.positionId()));

            Delegate delegate = new Delegate();
            delegate.setSchool(school);
            delegate.setPosition(position);
            schoolService.saveDelegate(delegate);
        }

        return null;
    }
}
