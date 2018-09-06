package org.brownmun.cli.commands;

import org.brownmun.core.school.AdvisorService;
import org.brownmun.core.school.model.Advisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import org.brownmun.core.school.SchoolService;
import org.brownmun.core.school.model.School;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciithemes.a8.A8_Grids;

@ShellComponent
public class SchoolCommands
{
    private final SchoolService schoolService;
    private final AdvisorService advisorService;

    @Autowired
    public SchoolCommands(SchoolService schoolService, AdvisorService advisorService)
    {
        this.schoolService = schoolService;
        this.advisorService = advisorService;
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

        schoolService.listSchools().stream().filter(s -> s.getName().toLowerCase().contains(queryLower))
                .forEach(school -> {
                    table.addRow(school.getId(), school.getName(), school.isAccepted());
                    table.addRule();
                });

        return table.render();
    }

    @ShellMethod("Create an advisor")
    public long createAdvisor(long schoolId, String name, String password, String email, String phoneNumber)
    {
        return advisorService.createAdvisor(schoolId, name, password, email, phoneNumber).getId();
    }

    @ShellMethod("Reset advisor password")
    public void resetAdvisorPassword(String advisorEmail, String password)
    {
        Advisor advisor = schoolService
                .findAdvisor(advisorEmail)
                .orElseThrow(() -> new IllegalArgumentException("Advisor not found"));

        advisorService.changePassword(advisor, password);
    }
}
