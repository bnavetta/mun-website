package org.brownmun.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * A hotel BUSUN has a relationship with
 */
@Entity
public class Hotel
{
    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(Locale.US);

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

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public LocalDate getDeadline()
    {
        return deadline;
    }

    public void setDeadline(LocalDate deadline)
    {
        this.deadline = deadline;
    }

    public Double getRate()
    {
        return rate;
    }

    public void setRate(Double rate)
    {
        this.rate = rate;
    }

    public Integer getAvailability()
    {
        return availability;
    }

    public void setAvailability(Integer availability)
    {
        this.availability = availability;
    }

    public String getBookingPage()
    {
        return bookingPage;
    }

    public void setBookingPage(String bookingPage)
    {
        this.bookingPage = bookingPage;
    }

    public String getImageAddress()
    {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress)
    {
        this.imageAddress = imageAddress;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hotel hotel = (Hotel) o;
        return Objects.equals(id, hotel.id) &&
                Objects.equals(name, hotel.name) &&
                Objects.equals(deadline, hotel.deadline) &&
                Objects.equals(rate, hotel.rate) &&
                Objects.equals(availability, hotel.availability) &&
                Objects.equals(bookingPage, hotel.bookingPage) &&
                Objects.equals(imageAddress, hotel.imageAddress) &&
                Objects.equals(address, hotel.address) &&
                Objects.equals(phoneNumber, hotel.phoneNumber);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, name, deadline, rate, availability, bookingPage, imageAddress, address, phoneNumber);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("deadline", deadline)
                .add("rate", rate)
                .add("availability", availability)
                .add("bookingPage", bookingPage)
                .add("imageAddress", imageAddress)
                .add("address", address)
                .add("phoneNumber", phoneNumber)
                .toString();
    }
}
