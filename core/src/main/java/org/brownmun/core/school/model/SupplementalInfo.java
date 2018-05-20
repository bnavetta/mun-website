package org.brownmun.core.school.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.brownmun.core.logistics.model.Hotel;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * A school's supplemental information, filled out after they're accepted.
 */
@Entity
public class SupplementalInfo
{
    @JsonIgnore
    @Id
    private Long id;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private School school;

    // @PhoneNumber
    // @NotBlank
    private String phoneNumber;

    // @NotBlank
    private String streetAddress;

    // @NotBlank
    private String city;

    private String state;

    private String postalCode;

    // @NotBlank
    private String country;

    private boolean financialAid;

    // @Min(0)
    private double financialAidAmount = 0;
    private String financialAidDocumentation;

    @ManyToOne
    @JoinColumn(name = "busun_hotel_id")
    private Hotel busunHotel;

    private String nonBusunHotel;

    private boolean shuttleFridayAfternoon;
    private boolean shuttleFridayNight;
    private boolean shuttleSaturday;
    private boolean shuttleSunday;

    private boolean commuting;

    /*
     * These are how many cars/buses are coming and how many days they'll be parked. So the number of car parking passes
     * is {@code carsParking * carParkingDays}, and the same for buses.
     */

    // @Min(0)
    private int carsParking;

    // @Min(0)
    private int carParkingDays;

    // @Min(0)
    private int busParking;

    // @Min(0)
    private int busParkingDays;

    // Would be nice to have this be a LocalTime, but we've had issues with parsing time info from the user forms.
    private String arrivalTime;

    @Type(type = "org.brownmun.core.db.PostgresEnumType", parameters = @org.hibernate.annotations.Parameter(name = "postgres_enum", value = "luggage_storage"))
    @Enumerated(EnumType.STRING)
    private LuggageStorage luggageStorage;

    /**
     * If true, delegates can only leave the social with an advisor. If false, they can leave accompanied by a staff
     * member.
     */
    private boolean delegateSocialNeedAdvisor;

    // @Min(0)
    private int delegateCount;

    // @Min(0)
    private int chaperoneCount;

    private String chaperoneInfo;

    // @Min(0)
    private int parliProTrainingCount;

    // @Min(0)
    private int crisisTrainingCount;

    // @Min(0)
    private int tourCount;

    // @Min(0)
    private int infoSessionCount;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public School getSchool()
    {
        return school;
    }

    public void setSchool(School school)
    {
        this.school = school;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getStreetAddress()
    {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress)
    {
        this.streetAddress = streetAddress;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getPostalCode()
    {
        return postalCode;
    }

    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public boolean isFinancialAid()
    {
        return financialAid;
    }

    public void setFinancialAid(boolean financialAid)
    {
        this.financialAid = financialAid;
    }

    public double getFinancialAidAmount()
    {
        return financialAidAmount;
    }

    public void setFinancialAidAmount(double financialAidAmount)
    {
        this.financialAidAmount = financialAidAmount;
    }

    public String getFinancialAidDocumentation()
    {
        return financialAidDocumentation;
    }

    public void setFinancialAidDocumentation(String financialAidDocumentation)
    {
        this.financialAidDocumentation = financialAidDocumentation;
    }

    public Hotel getBusunHotel()
    {
        return busunHotel;
    }

    public void setBusunHotel(Hotel busunHotel)
    {
        this.busunHotel = busunHotel;
    }

    public String getNonBusunHotel()
    {
        return nonBusunHotel;
    }

    public void setNonBusunHotel(String nonBusunHotel)
    {
        this.nonBusunHotel = nonBusunHotel;
    }

    public boolean isCommuting()
    {
        return commuting;
    }

    public void setCommuting(boolean commuting)
    {
        this.commuting = commuting;
    }

    public int getCarsParking()
    {
        return carsParking;
    }

    public void setCarsParking(int carsParking)
    {
        this.carsParking = carsParking;
    }

    public int getCarParkingDays()
    {
        return carParkingDays;
    }

    public void setCarParkingDays(int carParkingDays)
    {
        this.carParkingDays = carParkingDays;
    }

    public int getBusParking()
    {
        return busParking;
    }

    public void setBusParking(int busParking)
    {
        this.busParking = busParking;
    }

    public int getBusParkingDays()
    {
        return busParkingDays;
    }

    public void setBusParkingDays(int busParkingDays)
    {
        this.busParkingDays = busParkingDays;
    }

    public String getArrivalTime()
    {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime)
    {
        this.arrivalTime = arrivalTime;
    }

    public LuggageStorage getLuggageStorage()
    {
        return luggageStorage;
    }

    public void setLuggageStorage(LuggageStorage luggageStorage)
    {
        this.luggageStorage = luggageStorage;
    }

    public boolean isDelegateSocialNeedAdvisor()
    {
        return delegateSocialNeedAdvisor;
    }

    public void setDelegateSocialNeedAdvisor(boolean delegateSocialNeedAdvisor)
    {
        this.delegateSocialNeedAdvisor = delegateSocialNeedAdvisor;
    }

    public int getDelegateCount()
    {
        return delegateCount;
    }

    public void setDelegateCount(int delegateCount)
    {
        this.delegateCount = delegateCount;
    }

    public int getChaperoneCount()
    {
        return chaperoneCount;
    }

    public void setChaperoneCount(int chaperoneCount)
    {
        this.chaperoneCount = chaperoneCount;
    }

    public int getParliProTrainingCount()
    {
        return parliProTrainingCount;
    }

    public void setParliProTrainingCount(int parliProTrainingCount)
    {
        this.parliProTrainingCount = parliProTrainingCount;
    }

    public int getCrisisTrainingCount()
    {
        return crisisTrainingCount;
    }

    public void setCrisisTrainingCount(int crisisTrainingCount)
    {
        this.crisisTrainingCount = crisisTrainingCount;
    }

    public int getTourCount()
    {
        return tourCount;
    }

    public void setTourCount(int tourCount)
    {
        this.tourCount = tourCount;
    }

    public int getInfoSessionCount()
    {
        return infoSessionCount;
    }

    public void setInfoSessionCount(int infoSessionCount)
    {
        this.infoSessionCount = infoSessionCount;
    }

    public boolean isShuttleFridayAfternoon()
    {
        return shuttleFridayAfternoon;
    }

    public void setShuttleFridayAfternoon(boolean shuttleFridayAfternoon)
    {
        this.shuttleFridayAfternoon = shuttleFridayAfternoon;
    }

    public boolean isShuttleFridayNight()
    {
        return shuttleFridayNight;
    }

    public void setShuttleFridayNight(boolean shuttleFridayNight)
    {
        this.shuttleFridayNight = shuttleFridayNight;
    }

    public boolean isShuttleSaturday()
    {
        return shuttleSaturday;
    }

    public void setShuttleSaturday(boolean shuttleSaturday)
    {
        this.shuttleSaturday = shuttleSaturday;
    }

    public boolean isShuttleSunday()
    {
        return shuttleSunday;
    }

    public void setShuttleSunday(boolean shuttleSunday)
    {
        this.shuttleSunday = shuttleSunday;
    }

    public String getChaperoneInfo()
    {
        return chaperoneInfo;
    }

    public void setChaperoneInfo(String chaperoneInfo)
    {
        this.chaperoneInfo = chaperoneInfo;
    }

    public enum LuggageStorage
    {
        FRIDAY,
        SUNDAY,
        BOTH,
        NONE
    }

}
