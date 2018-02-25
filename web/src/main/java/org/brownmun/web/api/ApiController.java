package org.brownmun.web.api;

import org.brownmun.core.committee.CommitteeService;
import org.brownmun.db.committee.Committee;
import org.brownmun.db.committee.CommitteeType;
import org.brownmun.db.committee.JointCrisis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Read-only API for client-side code on the public facing website, such as the committees list.
 */
@RestController
@RequestMapping("/api")
public class ApiController
{
    private final CommitteeService committees;

    @Autowired
    public ApiController(CommitteeService committees)
    {
        this.committees = committees;
    }

    @GetMapping("/committee")
    public CommitteeListing listCommittees()
    {
        List<Committee> general = committees.allByType(CommitteeType.GENERAL);
        List<Committee> spec = committees.allByType(CommitteeType.SPECIALIZED);
        List<Committee> crisis = committees.nonJointCrises();
        List<JointCrisis> jointCrises = committees.jointCrises();

        return CommitteeListing.create(general, spec, crisis, jointCrises);
    }
}
