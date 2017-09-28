package org.brownmun.web.admin;

import com.fasterxml.jackson.annotation.JsonView;
import org.brownmun.model.committee.CommitteeType;
import org.brownmun.model.committee.Position;
import org.brownmun.model.repo.CommitteeRepository;
import org.brownmun.model.repo.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/adminp/assignment-params")
public class AssignmentParamsController
{
    private final CommitteeRepository committees;
    private final PositionRepository positions;

    @Autowired
    public AssignmentParamsController(CommitteeRepository committees, PositionRepository positions)
    {
        this.committees = committees;
        this.positions = positions;
    }

    @GetMapping("/ga-countries")
    public Map<String, List<Long>> getGeneralAssemblyCountries()
    {
        return positions.findByCommitteeType(CommitteeType.GENERAL)
                .collect(Collectors.groupingBy(Position::getName, Collectors.mapping(Position::getId, Collectors.toList())));
    }

    @GetMapping("/spec-positions")
    @JsonView(Position.AssignmentView.class)
    public List<Position> getSpecPositions()
    {
        return positions.findByCommitteeType(CommitteeType.SPECIALIZED).collect(Collectors.toList());
    }

    @GetMapping("/crisis-positions")
    @JsonView(Position.AssignmentView.class)
    public List<Position> getCrisisPositions()
    {
        return positions.findByCommitteeType(CommitteeType.CRISIS).collect(Collectors.toList());
    }
}
