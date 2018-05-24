package org.brownmun.cli.commands;

import java.util.stream.Collectors;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciithemes.a8.A8_Grids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import org.brownmun.core.school.SchoolService;
import org.brownmun.core.school.model.School;

@ShellComponent
public class SchoolCommands
{
    private final SchoolService schoolService;

    @Autowired
    public SchoolCommands(SchoolService schoolService)
    {
        this.schoolService = schoolService;
    }

    @ShellMethod("Register a new school")
    public long registerSchool(String name, String advisorName, String advisorPassword, String advisorEmail)
    {
        // TODO: don't make my cell phone the placeholder
        School school = schoolService.registerSchool(name, advisorName, advisorEmail, advisorPassword, "978-302-3343");
        schoolService.submitApplication(school);
        return school.getId();
    }

    @ShellMethod("Find schools by name")
    public String findSchools(String nameQuery)
    {
        String queryLower = nameQuery.toLowerCase();

        AsciiTable table = new AsciiTable();
        table.getContext().setGrid(A8_Grids.lineDoubleBlocks());
        table.addRow("School ID", "School Name", "Accepted");
        table.addStrongRule();

        schoolService.listSchools().stream().filter(s -> s.getName().toLowerCase().contains(queryLower)).forEach(school -> {
            table.addRow(school.getId(), school.getName(), school.isAccepted());
            table.addRule();
        });

        return table.render();
    }
}
