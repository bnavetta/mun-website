package org.brownmun.core.committee.impl;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

import org.brownmun.core.committee.CommitteeListing;
import org.brownmun.core.committee.CommitteeService;
import org.brownmun.core.committee.model.Committee;
import org.brownmun.core.committee.model.CommitteeType;
import org.brownmun.core.committee.model.Position;
import org.brownmun.core.school.model.Delegate;

@Service
public class CommitteeServiceImpl implements CommitteeService
{
    private static final Logger log = LoggerFactory.getLogger(CommitteeServiceImpl.class);

    private final CommitteeRepository repo;
    private final PositionRepository positionRepo;

    @Autowired
    public CommitteeServiceImpl(CommitteeRepository repo, PositionRepository positionRepo)
    {
        this.repo = repo;
        this.positionRepo = positionRepo;
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

    @Override
    public Optional<Position> getPosition(long id)
    {
        return positionRepo.findById(id);
    }

    @Override
    public Delegate assignPosition(long positionId, long schoolId)
    {
        Position position = positionRepo.getOne(positionId);

        Delegate old = position.getDelegate();
        if (old != null)
        {
            log.info("Unassigning {} from {}", position, old);
            old.setPosition(null);
        }

        // TODO: do we delete the old delegate? should there ever be delegates w/o
        // positions
        // or are they basically just a join table?
        // by the time a delegate has attendance info, the conference has already
        // started
        // if a delegate has a name but we unassign their position, it's probably
        // because they're not coming
        // there could be position swaps, but maybe we implement that separately
        // so just treat delegates as join table

        return null;
    }
}
