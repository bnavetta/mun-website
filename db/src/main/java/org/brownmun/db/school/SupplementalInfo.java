package org.brownmun.db.school;

import javax.persistence.*;

/**
 * A school's supplemental information, filled out after they're
 * accepted.
 */
@Entity
public class SupplementalInfo
{
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private School school;
}
