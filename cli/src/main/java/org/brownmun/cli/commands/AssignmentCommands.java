package org.brownmun.cli.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import org.brownmun.cli.positions.SchoolAllocation;
import org.brownmun.cli.positions.allocation.Allocator;
import org.brownmun.cli.positions.assignment.Assigner;
import org.brownmun.cli.positions.assignment.PositionAssignment;
import org.brownmun.core.committee.CommitteeService;
import org.brownmun.core.school.SchoolService;
import org.brownmun.core.school.model.Delegate;
import org.brownmun.core.school.model.School;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciithemes.a8.A8_Grids;

@ShellComponent
public class AssignmentCommands
{
    private final SchoolService schoolService;
    private final CommitteeService committeeService;
    private final Allocator allocator;
    private final Assigner assigner;
    private final CsvMapper csvMapper;

    @Autowired
    public AssignmentCommands(SchoolService schoolService, CommitteeService committeeService, Allocator allocator,
            Assigner assigner, CsvMapper csvMapper)
    {
        this.schoolService = schoolService;
        this.committeeService = committeeService;
        this.allocator = allocator;
        this.assigner = assigner;
        this.csvMapper = csvMapper;
    }

    @ShellMethod("Generate committee type allocations")
    public String generateAllocations(File preferences, File output) throws IOException
    {
        List<SchoolAllocation> allocations = allocator.generateAllocations(preferences, output);

        AsciiTable table = new AsciiTable();
        table.getContext().setGrid(A8_Grids.lineDoubleBlocks());
        table.addRow("School ID", "School Name", "General", "Specialized", "Crisis");
        table.addStrongRule();
        for (SchoolAllocation allocation : allocations)
        {
            School school = schoolService.getSchool(allocation.id()).orElseThrow();
            table.addRow(allocation.id(), school.getName(), allocation.general(), allocation.specialized(),
                    allocation.crisis());
            table.addRule();
        }

        return table.render();
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

        for (PositionAssignment assignment : assignments)
        {
            School school = schoolService.getSchool(assignment.schoolId())
                    .orElseThrow(() -> new RuntimeException("No school with ID " + assignment.schoolId()));

            // Position position = committeeService.getCommittee();

            Delegate delegate = new Delegate();
            delegate.setSchool(school);

        }

        return null;
    }
}
