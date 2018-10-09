package org.brownmun.web.api;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;
import org.brownmun.core.Conference;
import org.brownmun.core.committee.model.Committee;
import org.brownmun.core.committee.model.CommitteeType;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.brownmun.core.committee.CommitteeService;
import org.brownmun.core.logistics.HotelService;
import org.brownmun.core.logistics.model.Hotel;

/**
 * Read-only API for client-side code on the public facing website, such as the
 * committees list.
 */
@RestController
@RequestMapping("/api")
public class ApiController
{
    private final CommitteeService committees;
    private final HotelService hotels;
    private final Conference conference;

    @Autowired
    public ApiController(CommitteeService committees, HotelService hotels, Conference conference)
    {
        this.committees = committees;
        this.hotels = hotels;
        this.conference = conference;
    }

    @GetMapping("/committee")
    public CommitteeListing listCommittees()
    {
        List<Committee> general = committees.listByType(CommitteeType.GENERAL);
        List<Committee> specialized = committees.listByType(CommitteeType.SPECIALIZED);
        List<Committee> crisis = committees.listByType(CommitteeType.CRISIS);
        List<Committee> jointCrisis = committees.listByType(CommitteeType.JOINT_CRISIS);

        List<CommitteeDTO> generalDTOs = general.stream().map(this::committeeToDTO).collect(Collectors.toList());
        List<CommitteeDTO> specializedDTOs = specialized.stream().map(this::committeeToDTO).collect(Collectors.toList());
        List<CommitteeDTO> crisisDTOs = crisis.stream().map(this::committeeToDTO).collect(Collectors.toList());
        List<CommitteeDTO> jccDTOs = jointCrisis.stream().map(this::committeeToDTO).collect(Collectors.toList());

        Map<Long, Set<CommitteeDTO>> jccRooms = Maps.newHashMap();
        for (Committee jcc : jointCrisis)
        {
            Set<Committee> rooms = committees.getJointCrisisRooms(jcc);
            jccRooms.put(jcc.getId(), rooms.stream().map(this::committeeToDTO).collect(Collectors.toSet()));
        }

        return CommitteeListing.create(generalDTOs, specializedDTOs, crisisDTOs, jccDTOs, jccRooms);
    }

    @GetMapping("/hotels")
    public List<Hotel> getHotels()
    {
        return hotels.allHotels();
    }

    private CommitteeDTO committeeToDTO(Committee c)
    {
        CommitteeDTO d = new CommitteeDTO();
        d.setId(c.getId());
        d.setName(c.getName());
        d.setDescription(c.getDescription());
        d.setTopic1(c.getTopic1());
        d.setTopic2(c.getTopic2());
        d.setTopic3(c.getTopic3());
        d.setTopic4(c.getTopic4());
        d.setMapLatitude(c.getMapLatitude());
        d.setMapLongitude(c.getMapLongitude());
        d.setImage(c.getImage());
        d.setEmail(conference.getCommitteeEmail(c.getShortName()));
        if (conference.isGuidesPublished())
        {
            d.setBackgroundGuide(conference.getBackgroundGuideURL(c.getShortName()));
        }
        return d;
    }
}
