package org.brownmun.core.school.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.brownmun.core.school.SchoolService;
import org.brownmun.db.school.Advisor;
import org.brownmun.db.school.School;
import org.brownmun.db.school.SchoolRepository;
import org.brownmun.util.Tokens;

@Service
public class SchoolServiceImpl implements SchoolService
{
    private final SchoolRepository repo;

    @Autowired
    public SchoolServiceImpl(SchoolRepository repo)
    {
        this.repo = repo;
    }

    @Override
    public School registerSchool(Advisor initialAdvisor, String name)
    {
        if (initialAdvisor.getSchool() != null || initialAdvisor.getId() != null)
        {
            throw new IllegalArgumentException("Advisor account already exists");
        }

        School school = new School();
        school.setAccepted(false);
        school.setHasApplied(false);
        school.setName(name);
        school.setRegistrationCode(Tokens.generate(8));
        school.addAdvisor(initialAdvisor);

        return repo.save(school);
    }
}
