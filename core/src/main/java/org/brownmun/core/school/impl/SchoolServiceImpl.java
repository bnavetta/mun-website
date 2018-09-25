package org.brownmun.core.school.impl;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import org.brownmun.core.school.SchoolService;
import org.brownmun.core.school.model.*;
import org.brownmun.util.Tokens;

@Service
public class SchoolServiceImpl implements SchoolService
{
    private static final Logger log = LoggerFactory.getLogger(SchoolServiceImpl.class);

    private final PasswordEncoder passwordEncoder;
    private final SchoolRepository repo;
    private final AdvisorRepository advisorRepo;
    private final SchoolApplicationRepository appRepo;
    private final SupplementalInfoRepository infoRepo;
    private final EntityManagerFactory emf;
    private final EntityManager em;

    @Autowired
    public SchoolServiceImpl(PasswordEncoder passwordEncoder, SchoolRepository repo, AdvisorRepository advisorRepo,
            SchoolApplicationRepository appRepo, SupplementalInfoRepository infoRepo, EntityManagerFactory emf,
            EntityManager em)
    {
        this.passwordEncoder = passwordEncoder;
        this.repo = repo;
        this.advisorRepo = advisorRepo;
        this.appRepo = appRepo;
        this.infoRepo = infoRepo;
        this.emf = emf;
        this.em = em;
    }

    @Override
    public Optional<School> getSchool(long id)
    {
        return repo.findById(id);
    }

    @Override
    public School registerSchool(String name, String advisorName, String advisorEmail, String advisorPassword,
            String advisorPhoneNumber)
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
        log.info("Submitting application for school {} ({})", school.getName(), school.getId());

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

    @Override
    public OptionalLong findSchoolId(long delegateId)
    {
        return repo.findSchoolId(delegateId);
    }

    @Override
    @Transactional
    public School loadSchool(Advisor advisor)
    {
        School school = repo.getOne(advisor.getSchool().getId());
        log.info("Materialized school {}", school.toString());
        return (School) Hibernate.unproxy(school);
    }

    @Override
    @Transactional
    public void updateApplication(Advisor advisor, SchoolApplication application)
    {
        log.info("Application for {} being updated by {}", application.getName(), advisor.getEmail());
        appRepo.save(application);
        emf.getCache().evict(School.class, application.getId());
        emf.getCache().evict(SchoolStatus.class, application.getId());
    }

    @Override
    public void accept(School school) {
        log.info("Accepting {} ({})", school.getId(), school.getName());
        school.setAccepted(true);
        repo.save(school);
    }

    @Override
    public List<School> listSchools()
    {
        return repo.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public List<Advisor> getAdvisors(long schoolId)
    {
        return advisorRepo.findBySchoolId(schoolId);
    }

    @Override
    public List<Advisor> listAdvisors()
    {
        return advisorRepo.findAll();
    }

    /**
     * Fetches the school's supplemental info, creating a new object if necessary.
     */
    @Override
    public SupplementalInfo getSupplementalInfo(long schoolId)
    {
        return infoRepo.findById(schoolId).orElseGet(() -> {
            SupplementalInfo info = new SupplementalInfo();
            info.setSchool(repo.getOne(schoolId));
            return info;
        });
    }

    @Transactional
    @Override
    public SupplementalInfo updateSupplementalInfo(Advisor advisor, SupplementalInfo info)
    {
        /*
         * Because we're using @MapsId on SupplementalInfo, Spring thinks we're always
         * updating an existing entity. To work around that, we first check if we're
         * *actually* updating an entity or need to persist a new one.
         */

        School school = loadSchool(advisor);

        log.info("Updating supplemental information for school {}", school.getName());
        info.setId(school.getId());
        info.setSchool(school);

        Optional<SupplementalInfo> existing = infoRepo.findById(school.getId());
        if (existing.isPresent())
        {
            return em.merge(info);
        }
        else
        {
            em.persist(info);
            return info;
        }
    }
}
