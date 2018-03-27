package org.brownmun.web.info;

import org.brownmun.core.committee.CommitteeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/committees")
public class CommitteeController
{
    private static final Logger log = LoggerFactory.getLogger(CommitteeController.class);

    private final CommitteeService committees;

    @Autowired
    public CommitteeController(CommitteeService committees)
    {
        this.committees = committees;
    }

    @GetMapping
    public String list()
    {
        return "committees/list";
    }
}
