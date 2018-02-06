package org.brownmun.web.info;

import org.brownmun.core.committee.CommitteeService;
import org.brownmun.db.committee.Committee;
import org.brownmun.db.committee.CommitteeType;
import org.brownmun.db.committee.JointCrisis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

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
    public ModelAndView list()
    {
        List<Committee> general = committees.allByType(CommitteeType.GENERAL);
        List<Committee> spec = committees.allByType(CommitteeType.SPECIALIZED);
        List<Committee> crisis = committees.nonJointCrises();
        List<JointCrisis> jointCrises = committees.jointCrises();

        return new ModelAndView("committees/list", Map.of(
                "general", general,
                "spec", spec,
                "crisis", crisis,
                "jointCrises", jointCrises
        ));
    }
}
