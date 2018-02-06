package org.brownmun.web.admin.awards;

import com.google.common.collect.Lists;
import org.brownmun.model.committee.Award;
import org.brownmun.model.committee.AwardInfo;
import org.brownmun.model.committee.AwardType;
import org.brownmun.model.repo.AwardsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AwardsService
{
    private static final String INSERT_SQL = "INSERT INTO award (committee_id, award_type) VALUES (?, ?)";

    private final JdbcTemplate jdbc;
    private final AwardsRepository repo;

    @Autowired
    public AwardsService(JdbcTemplate jdbc, AwardsRepository repo)
    {
        this.jdbc = jdbc;
        this.repo = repo;
    }

    public List<AwardInfo> getAllAwards()
    {
        return repo.findAwardInfo();
    }

    @Transactional
    public void saveAwards(List<Award> awards)
    {
        repo.save(awards);
        repo.flush();
    }

    @Transactional
    public void createAwards(AwardsBreakdown breakdown)
    {
        // Use straight JDBC so we can insert using the committee ID instead of having to fetch every committee

        // Start from scratch
        jdbc.execute("DELETE FROM award");

        List<Object[]> awards = Lists.newArrayListWithExpectedSize(4 * breakdown.getCommittees().size());
        for (AwardsBreakdown.Entry e : breakdown.getCommittees())
        {
            for (int i = 0; i < e.getBestDelegateCount(); i++)
            {
                awards.add(new Object[]{ e.getCommitteeId(), AwardType.BEST_DELEGATE.name() });
            }

            for (int i = 0; i < e.getOutstandingDelegateCount(); i++)
            {
                awards.add(new Object[]{ e.getCommitteeId(), AwardType.OUTSTANDING_DELEGATE.name() });
            }

            for (int i = 0; i < e.getHonorableDelegateCount(); i++)
            {
                awards.add(new Object[]{ e.getCommitteeId(), AwardType.HONORABLE_DELEGATE.name() });
            }
        }

        jdbc.batchUpdate(INSERT_SQL, awards);
    }
}
