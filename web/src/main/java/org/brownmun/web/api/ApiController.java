package org.brownmun.web.api;

import org.brownmun.core.committee.CommitteeListing;
import org.brownmun.core.committee.CommitteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Read-only API for client-side code on the public facing website, such as the
 * committees list.
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
        return committees.list();
    }
}
