package org.brownmun.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * A MUN committee, like NATO
 */
@Data
@Entity
@EqualsAndHashCode(exclude = {"positions"})
public class Committee
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String name;

	@NotBlank
	private String description;

	@NotBlank
	private String shortName;

	private boolean jointCrisis;

	private String topic1;

	private String topic2;

	private String topic3;

	private String topic4;

	@NotNull
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
