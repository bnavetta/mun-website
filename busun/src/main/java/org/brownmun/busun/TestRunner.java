package org.brownmun.busun;

import lombok.extern.slf4j.Slf4j;
import org.brownmun.model.*;
import org.brownmun.model.repo.CommitteeRepository;
import org.brownmun.model.repo.DelegateRepository;
import org.brownmun.model.repo.PositionRepository;
import org.brownmun.model.repo.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Testing stuff out
 */
@Slf4j
@Component
public class TestRunner implements CommandLineRunner
{
	@Autowired
	private CommitteeRepository committees;

	@Autowired
	private DelegateRepository delegates;

	@Autowired
	private PositionRepository positions;

	@Autowired
	private SchoolRepository schools;

	@Transactional
	@Override
	public void run(String... args) throws Exception
	{
		log.info("Running test logic");

//		positions.deleteAll();
//		committees.deleteAll();
//		delegates.deleteAll();
//		schools.deleteAll();
//
//		School school = new School();
//		school.setName("Test School");
//		school = schools.save(school);
//
//		Committee committee = new Committee();
//		committee.setName("Hungarian Revolution - NATO");
//		committee.setCommitteeType(CommitteeType.CRISIS);
//		committee.setDescription("Adam's committee");
//		committee = committees.save(committee);
//
//		Position position = new Position();
//		position.setName("Benevolent Dictator for Life");
//		position.setCommittee(committee);
//		position = positions.save(position);
//
//		Delegate delegate = new Delegate();
//		delegate.setName("Adam DeHovitz");
//		delegate.setSchool(school);
//		position.setDelegate(delegate);
//		delegates.save(delegate);
//		positions.save(position);

		System.out.println("FINDING A POSITION BY NAMES");
		System.out.println(positions.findByNameAndCommitteeName("Benevolent Dictator for Life", "Hungarian Revolution - NATO"));

		System.out.println("ASSIGNED POSITIONS");
		for (Committee c : committees.findAll()) {
			System.out.printf("%s has %d assigned positions\n", c.getName(), c.getAssignedPositions());
		}

		System.out.println("IS ASSIGNED");
		for (Position p : positions.findAll()) {
			System.out.printf("Position %s %s assigned\n", p.getName(), p.isAssigned() ? "is" : "is not");
		}
	}
}
