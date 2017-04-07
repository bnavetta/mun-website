package org.brownmun.model.delegation;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.brownmun.model.Hotel;
import org.brownmun.model.School;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalTime;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * Supplemental/logistical information about a school
 */
@Data
@Entity
public class SchoolInfo
{
    @Id
    private Long id;

    @JsonView(School.View.Summary.class)
    @Valid
    @Embedded
    private Address address;

    @JsonView(School.View.Summary.class)
    @NotBlank
    private String phoneNumber;

    /*
     * Hotel Info
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @Setter(AccessLevel.NONE)
    @Formula("(hotel_id is not null)")
    private boolean usingBusunHotel;

    private String otherHotel;

    /*
     * Shuttle/Transportation Info
     */

    private Boolean usingShuttle;

    private Boolean commuting;

    @Min(0)
    private int carCount;

    @Min(0)
    private int carDays;

    @Min(0)
    private int busCount;

    @Min(0)
    private int busDays;

    @Embedded
    @Valid
    private ShuttleService shuttleService;

    /*
     * Misc. logistics
     */
    private LocalTime arrivalTime;

    @Enumerated(EnumType.STRING)
    private LuggageStorage luggageStorage;

    @Enumerated(EnumType.STRING)
    private BandColor bandColor;

    /*
     * Delegation Info
     */

    @Min(1)
    @JsonView(School.View.Summary.class)
    private Integer requestedDelegates;

    @Min(1)
    private Integer requestedChaperones;

    @Min(0)
    private Integer parliTrainingCount;

    @Min(0)
    private Integer crisisTrainingCount;

    @Min(0)
    private Integer tourCount;

    @Min(0)
    private Integer infoSessionCount;

    /*
     * Names and phone numbers of chaperones. Since it's just to display, probably not
     * worth a whole class
     */
    private String chaperoneInfo;
}
