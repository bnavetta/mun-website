package org.brownmun.core.committee.impl;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

import org.brownmun.core.committee.CommitteeListing;
import org.brownmun.core.committee.CommitteeService;
import org.brownmun.core.committee.model.Committee;
import org.brownmun.core.committee.model.CommitteeRepository;
import org.brownmun.core.committee.model.CommitteeType;
import org.brownmun.core.committee.model.Position;

@Service
public class CommitteeServiceImpl implements CommitteeService
{
    private final CommitteeRepository repo;

    @Autowired
    public CommitteeServiceImpl(CommitteeRepository repo)
    {
        this.repo = repo;
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
    public Stream<Committee> allByType(CommitteeType type)
    {
        return repo.findAllByTypeOrderByNameAsc(type);
    }

    @Transactional
    @Override
    public CommitteeListing list()
    {
        List<Committee> general = allByType(CommitteeType.GENERAL).collect(Collectors.toList());
        List<Committee> specialized = allByType(CommitteeType.SPECIALIZED).collect(Collectors.toList());
        List<Committee> crisis = allByType(CommitteeType.CRISIS).collect(Collectors.toList());
        List<Committee> jointCrisis = allByType(CommitteeType.JOINT_CRISIS).collect(Collectors.toList());

        Map<Long, Set<Committee>> jccRooms = Maps.newHashMap();
        for (Committee jcc : jointCrisis)
        {
            // Hibernate 5.2 breaks Jackson's hibernate5 datatype module (which mostly knows
            // how to un-proxy/initialize
            // things), so we explicitly initialize here.
            Set<Committee> rooms = jcc.getJointCrisisRooms();
            Hibernate.initialize(rooms);
            jccRooms.put(jcc.getId(), rooms);
        }

        return CommitteeListing.create(general, specialized, crisis, jointCrisis, jccRooms);
    }

    @Override
    public OptionalLong findCommitteeId(long positionId)
    {
        return repo.findCommitteeId(positionId);
    }

    @Override
    public Collection<Position> getPositions(Committee c)
    {
        return repo.fetchPositions(c);
    }

    @Override
    public Optional<Committee> getCommittee(long id)
    {
        return repo.findById(id);
    }
}
