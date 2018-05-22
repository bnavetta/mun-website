package org.brownmun.cli.commands;

import java.util.stream.Collectors;

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
    public Iterable<School> findSchools(String nameQuery)
    {
        String queryLower = nameQuery.toLowerCase();
        return schoolService.listSchools().stream().filter(s -> s.getName().toLowerCase().contains(queryLower))
                .collect(Collectors.toList());
    }
}
