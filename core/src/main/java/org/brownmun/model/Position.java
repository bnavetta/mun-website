package org.brownmun.model;

import com.google.common.base.MoreObjects;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.*;

/**
 * A specific position on a committee, which may be assigned to delegates.
 */
@Data
@Entity
public class Position
{
	@Setter(AccessLevel.NONE)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "committee_id")
	private Committee committee;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "delegate_id")
	private Delegate delegate;

	@Setter(AccessLevel.NONE)
	@Formula("(delegate_id is not null)")
	private boolean assigned;

	@Override
	public String toString()
	{
		// Use ids to prevent infinite toString recursion
		return MoreObjects.toStringHelper(this)
			.add("id", id)
			.add("name", name)
			.add("committee_id", committee.getId())
			.add("delegate_id", delegate.getId())
			.add("assigned", assigned)
			.toString();
	}
}
