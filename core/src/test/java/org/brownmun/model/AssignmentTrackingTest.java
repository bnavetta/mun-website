package org.brownmun.model;

import org.brownmun.BaseSpringTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

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
		school = new School();
		school.setName("Test School");
		entityManager.persistAndFlush(school);

		committee = new Committee();
		committee.setName("Test Committe");
		committee.setCommitteeType(CommitteeType.CRISIS);
		committee.setDescription("A committee for testing");
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
}
