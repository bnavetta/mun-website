package org.brownmun.web;

import org.brownmun.core.committee.CommitteeService;
import org.brownmun.db.committee.Committee;
import org.brownmun.db.committee.CommitteeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class TestController
{
    private final CommitteeService committees;

    @Autowired
    public TestController(CommitteeService committees)
    {
        this.committees = committees;
    }


    @GetMapping
    public String index()
    {
        return "index!";
    }

    @GetMapping("/create")
    @ResponseBody
    public Committee createCommittee()
    {
        Committee c = new Committee();
        c.setName("Test Committee");
        c.setShortName("test");
        c.setType(CommitteeType.GENERAL);
        return committees.save(c);
    }
}
