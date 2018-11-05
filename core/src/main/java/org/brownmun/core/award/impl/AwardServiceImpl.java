package org.brownmun.core.award.impl;

import org.brownmun.core.award.AwardService;
import org.brownmun.core.award.model.Award;
import org.brownmun.core.committee.CommitteeService;
import org.brownmun.core.committee.model.Committee;
import org.brownmun.core.committee.model.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AwardServiceImpl implements AwardService
{
    private static final Logger log = LoggerFactory.getLogger(AwardServiceImpl.class);

    private final CommitteeService committeeService;
    private final AwardRepository awardRepository;

    @Autowired
    public AwardServiceImpl(CommitteeService committeeService, AwardRepository awardRepository)
    {
        this.committeeService = committeeService;
        this.awardRepository = awardRepository;
    }

    @Override
    public List<Award> awardsForCommittee(long committeeId)
    {
        Committee committee = committeeService.getCommittee(committeeId)
        .orElseThrow(() -> new IllegalArgumentException("No committee with ID" + committeeId));
        return awardRepository.findAllByCommittee(committee);
    }

    @Override
    @Transactional
    public Award setAward(long awardId, long positionId)
    {
        Award award = awardRepository.findById(awardId)
                .orElseThrow(() -> new IllegalArgumentException("No award with ID " + awardId));
        Position position = committeeService.getPosition(positionId)
                .orElseThrow(() -> new IllegalArgumentException("No position with ID" + positionId));

        if (!award.getCommittee().getId().equals(position.getCommittee().getId()))
        {
            throw new IllegalStateException("Cannot assign award to position in a different committee");
        }

        log.debug("Granting {} award {} in {} to {}", award.getType().getDisplayName(), award.getId(), award.getCommittee().getId(), position);

        award.setPosition(position);
        return awardRepository.save(award);
    }

    @Override
    @Transactional
    public Award unassignAward(long awardId)
    {
        Award award = awardRepository.findById(awardId)
                .orElseThrow(() -> new IllegalArgumentException("No award with ID " + awardId));
        log.debug("Unassigning {} award {} in {}", award.getType().getDisplayName(), award.getId(), award.getCommittee().getId());
        award.setPosition(null);
        return awardRepository.save(award);
    }

    @Override
    public Optional<Award> getAward(long id)
    {
        return awardRepository.findById(id);
    }
}