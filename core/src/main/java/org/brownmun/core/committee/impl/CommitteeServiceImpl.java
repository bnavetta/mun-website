package org.brownmun.core.committee.impl;

import java.util.List;

import com.google.common.collect.Lists;
import org.brownmun.core.committee.CommitteeListing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.brownmun.core.committee.CommitteeService;
import org.brownmun.db.committee.*;

@Service
public class CommitteeServiceImpl implements CommitteeService
{
    private final CommitteeRepository repo;
    private final JointCrisisRepository jointCrisisRepo;

    @Autowired
    public CommitteeServiceImpl(CommitteeRepository repo, JointCrisisRepository jointCrisisRepo)
    {
        this.repo = repo;
        this.jointCrisisRepo = jointCrisisRepo;
    }

    @Override
    public Committee save(Committee c)
    {
        return repo.save(c);
    }

    @Override
    public List<Committee> all()
    {
        return repo.findAll();
    }

    @Override
    public List<Committee> allByType(CommitteeType type)
    {
        return repo.findAllByTypeOrderByNameAsc(type);
    }

    @Override
    public List<JointCrisis> jointCrises()
    {
        return jointCrisisRepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public List<Committee> nonJointCrises()
    {
        return repo.findNonJointCrises();
    }

    @Override
    public CommitteeListing displayListing()
    {
        List<CommitteeDisplay> committees = repo.findAllForDisplay();

        List<CommitteeDisplay> general = Lists.newArrayList();
        List<CommitteeDisplay> specialized = Lists.newArrayList();
    }
}
