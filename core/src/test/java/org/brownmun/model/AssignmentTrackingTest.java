package org.brownmun.model;

import org.brownmun.BaseSpringTest;
import org.brownmun.model.committee.Committee;
import org.brownmun.model.committee.CommitteeType;
import org.brownmun.model.committee.Position;
import org.brownmun.model.delegation.Address;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
@RunWith(SpringRunner.class)
@BaseSpringTest
@DataJpaTest
public class AssignmentTrackingTest
{
	@Autowired
	private TestEntityManager entityManager;

	private School school;
	private Committee committee;

	@Before
	public void setup()
	{
		Address address = new Address();
		address.setCountry("United States");
		address.setStreetAddress("69 Brown Street");
		address.setState("Rhode Island");
		address.setCity("Providence");
		address.setPostalCode("02912");

		school = new School();
		school.setName("Test School");
		school.setStatus(RegistrationStatus.REGISTERED);
//		school.setPhoneNumber("123-456-7890");
		school.setRegistrationTime(Instant.now().minusSeconds(10000));
//		school.setAddress(address);
//		school.setRequestedDelegates(20);
//		school.setRequestedChaperones(2);
		entityManager.persistAndFlush(school);

		committee = new Committee();
		committee.setName("Test Committe");
		committee.setCommitteeType(CommitteeType.CRISIS);
		committee.setDescription("A committee for testing");
		committee.setShortName("TEST");
		entityManager.persistAndFlush(committee);
	}

	@After
	public void teardown()
	{
		entityManager.getEntityManager().createQuery("DELETE FROM Position").executeUpdate();
		entityManager.getEntityManager().createQuery("DELETE FROM Delegate").executeUpdate();
		entityManager.getEntityManager().createQuery("DELETE FROM School").executeUpdate();
		entityManager.getEntityManager().createQuery("DELETE FROM Committee").executeUpdate();
	}

	@Test
	public void testPositionIsAssigned()
	{
		Delegate delegate = new Delegate();
		delegate.setName("Test Delegate");
		delegate.setSchool(school);

		Position position = new Position();
		position.setName("Test Position");
		position.setCommittee(committee);
		position.setDelegate(delegate);

		entityManager.persistAndFlush(delegate);
		Position saved = entityManager.persistFlushFind(position);

		assertThat(saved.isAssigned()).isTrue();
	}

	@Test
	public void testPositionIsUnassigned()
	{
		Position position = new Position();
		position.setName("Test Position");
		position.setCommittee(committee);

		Position saved = entityManager.persistFlushFind(position);

		assertThat(saved.isAssigned()).isFalse();
	}

	@Test
	public void testCountAssignedPositions()
	{
		Delegate delegate = new Delegate();
		delegate.setName("Test Delegate");
		delegate.setSchool(school);
		delegate = entityManager.persistFlushFind(delegate);

		Position assigned = new Position();
		assigned.setName("Assigned position");
		assigned.setCommittee(committee);
		assigned.setDelegate(delegate);
		entityManager.persistAndFlush(assigned);

		Position unassigned = new Position();
		unassigned.setName("Unassigned positopn");
		unassigned.setCommittee(committee);
		entityManager.persistAndFlush(unassigned);

		entityManager.detach(committee);
		Committee fresh = entityManager.find(Committee.class, committee.getId());
		assertThat(fresh.getAssignedPositions()).isEqualTo(1);
	}
}
