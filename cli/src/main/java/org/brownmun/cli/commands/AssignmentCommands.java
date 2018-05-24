package org.brownmun.cli.commands;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciithemes.a8.A8_Grids;
import org.brownmun.cli.assignment.Allocator;
import org.brownmun.cli.assignment.SchoolAllocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.File;
import java.io.IOException;
import java.util.List;

@ShellComponent
public class AssignmentCommands
{
    private final Allocator allocator;

    @Autowired
    public AssignmentCommands(Allocator allocator)
    {
        this.allocator = allocator;
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
            table.addRow(allocation.id(), allocation.name(), allocation.general(), allocation.specialized(), allocation.crisis());
            table.addRule();
        }

        return table.render();
    }
}
