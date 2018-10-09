package org.brownmun.core.committee.impl;

import com.google.common.base.Preconditions;
import org.brownmun.core.committee.CommitteeService;
import org.brownmun.core.committee.model.Committee;
import org.brownmun.core.committee.model.CommitteeType;
import org.brownmun.core.committee.model.Position;
import org.brownmun.core.school.model.Delegate;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public Position savePosition(Position position)
    {
        if (position.getCommittee() == null)
        {
            throw new IllegalStateException("Position not assigned a committee");
        }

        return positionRepo.save(position);
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
    public List<Committee> listByType(CommitteeType type)
    {
        try (Stream<Committee> cs = repo.findAllByTypeOrderByNameAsc(type))
        {
            return cs.collect(Collectors.toList());
        }
    }

    @Transactional
    public Set<Committee> getJointCrisisRooms(Committee jointCrisis)
    {
        Preconditions.checkArgument(jointCrisis.getType() == CommitteeType.JOINT_CRISIS);
        Set<Committee> rooms = repo.getOne(jointCrisis.getId()).getJointCrisisRooms();
        Hibernate.initialize(rooms);
        return rooms;
    }

    @Override
    public OptionalLong findCommitteeId(long positionId)
    {
        return repo.findCommitteeId(positionId);
    }

    @Override
    @Transactional
    public Collection<Position> getPositions(Committee c)
    {
        return repo.fetchPositions(c);
    }

    @Override
    @Transactional
    public String getFullName(Committee c)
    {
        switch (c.getType())
        {
            case JOINT_CRISIS_ROOM:
                Committee jc = repo.findJointCrisis(c).orElseThrow(
                        () -> new IllegalStateException("Cannot find joint crisis containing " + c.getId()));
                return String.format("%s (%s)", c.getName(), jc.getName());
            default:
                return c.getName();
        }
    }

    @Override
    public Optional<Committee> getCommittee(long id)
    {
        return repo.findById(id);
    }

    @Override
    public Optional<Committee> findByShortName(String shortName)
    {
        return repo.findByShortName(shortName);
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
