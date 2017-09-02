package org.brownmun.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.net.URI;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import javax.persistence.*;

/**
 * A hotel BUSUN has a relationship with
 */
@Entity
@Data
public class Hotel
{
    private static NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(Locale.US);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private LocalDate deadline;

    private Double rate;

    private Integer availability;

    private String bookingPage;

    private String imageAddress;

    private String address;

    private String phoneNumber;

    @Transient
    @JsonIgnore
    public Date getDeadlineAsDate()
    {
        return Date.from(deadline.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @Transient
    @JsonIgnore
    public String getFormattedRate()
    {
        return CURRENCY_FORMATTER.format(rate);
    }
}
