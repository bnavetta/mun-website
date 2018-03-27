package org.brownmun.core.school.impl;

import com.google.common.base.Preconditions;
import org.brownmun.core.school.SchoolService;
import org.brownmun.core.school.model.*;
import org.brownmun.util.Tokens;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import java.util.Optional;

@Service
public class SchoolServiceImpl implements SchoolService
{
    private final PasswordEncoder passwordEncoder;
    private final SchoolRepository repo;
    private final AdvisorRepository advisorRepo;
    private final EntityManagerFactory emf;

    @Autowired
    public SchoolServiceImpl(PasswordEncoder passwordEncoder, SchoolRepository repo, AdvisorRepository advisorRepo, EntityManagerFactory emf)
    {
        this.passwordEncoder = passwordEncoder;
        this.repo = repo;
        this.advisorRepo = advisorRepo;
        this.emf = emf;
    }

    @Override
    public School registerSchool(String name, String advisorName, String advisorEmail, String advisorPassword, String advisorPhoneNumber)
    {
        School school = new School();
        school.setAccepted(false);
        school.setHasApplied(false);
        school.setName(name);
        school.setRegistrationCode(Tokens.generate(8));

        Advisor advisor = new Advisor();
        advisor.setName(advisorName);
        advisor.setEmail(advisorEmail);
        advisor.setPassword(passwordEncoder.encode(advisorPassword));
        advisor.setPhoneNumber(advisorPhoneNumber);
        school.addAdvisor(advisor);

        return repo.save(school);
    }

    @Override
    public void submitApplication(School school)
    {
        Preconditions.checkNotNull(school.getId(), "School does not exist");

        school.setHasApplied(true);
        repo.save(school);

        // Remove cached views of this school
        emf.getCache().evict(SchoolApplication.class, school.getId());
        emf.getCache().evict(SchoolStatus.class, school.getId());
    }

    @Override
    public Optional<Advisor> findAdvisor(String email)
    {
        return advisorRepo.findByEmail(email);
    }
}
