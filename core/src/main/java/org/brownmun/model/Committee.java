package org.brownmun.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import java.util.List;
import javax.persistence.*;

/**
 * A MUN committee, like NATO
 */
@Data
@Entity
@EqualsAndHashCode(exclude = {"positions"})
public class Committee
{
	@Setter(AccessLevel.NONE)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String description;

	@Enumerated(EnumType.STRING)
	private CommitteeType committeeType;

	@OneToMany(mappedBy = "committee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Position> positions;

	@Setter(AccessLevel.NONE)
	@Formula("(SELECT COUNT(*) FROM position p WHERE p.committee_id = id AND p.delegate_id IS NOT NULL)")
	private int assignedPositions;

	@Setter(AccessLevel.NONE)
	@Formula("(SELECT COUNT(*) FROM position p WHERE p.committee_id = id)")
	private int totalPositions;
}
