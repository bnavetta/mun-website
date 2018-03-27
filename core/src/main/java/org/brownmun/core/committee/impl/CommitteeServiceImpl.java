package org.brownmun.core.committee.impl;

import com.google.common.collect.Maps;
import org.brownmun.core.committee.CommitteeListing;
import org.brownmun.core.committee.CommitteeService;
import org.brownmun.core.committee.model.Committee;
import org.brownmun.core.committee.model.CommitteeRepository;
import org.brownmun.core.committee.model.CommitteeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public List<Committee> allByType(CommitteeType type)
    {
        return repo.findAllByTypeOrderByNameAsc(type);
    }

    @Transactional
    @Override
    public CommitteeListing list()
    {
        List<Committee> general = allByType(CommitteeType.GENERAL);
        List<Committee> specialized = allByType(CommitteeType.SPECIALIZED);
        List<Committee> crisis = allByType(CommitteeType.CRISIS);
        List<Committee> jointCrisis = allByType(CommitteeType.JOINT_CRISIS);

        Map<Long, Set<Committee>> jccRooms = Maps.newHashMap();
        for (Committee jcc : jointCrisis)
        {
            jccRooms.put(jcc.getId(), jcc.getJointCrisisRooms());
        }

        return CommitteeListing.create(general, specialized, crisis, jointCrisis, jccRooms);
    }
}
