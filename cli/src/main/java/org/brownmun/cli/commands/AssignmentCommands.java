package org.brownmun.cli.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import org.brownmun.cli.positions.SchoolAllocation;
import org.brownmun.cli.positions.allocation.Allocator;
import org.brownmun.cli.positions.assignment.Assigner;
import org.brownmun.cli.positions.assignment.PositionAssignment;
import org.brownmun.core.school.SchoolService;
import org.brownmun.core.school.model.School;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciithemes.a8.A8_Grids;

@ShellComponent
public class AssignmentCommands
{
    private final SchoolService schoolService;
    private final Allocator allocator;
    private final Assigner assigner;

    @Autowired
    public AssignmentCommands(SchoolService schoolService, Allocator allocator, Assigner assigner)
    {
        this.schoolService = schoolService;
        this.allocator = allocator;
        this.assigner = assigner;
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
}
