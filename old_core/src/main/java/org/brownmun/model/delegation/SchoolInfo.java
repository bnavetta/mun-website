package org.brownmun.model.delegation;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.base.MoreObjects;
import org.brownmun.model.Hotel;
import org.brownmun.model.School;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * Supplemental/logistical information about a school
 */
@Entity
public class SchoolInfo
{
    @Id
    private Long id;

    @JsonView(School.View.Summary.class)
//    @Valid
    @Embedded
    private Address address;

    @JsonView(School.View.Summary.class)
    private String phoneNumber;

    /*
     * Financial Aid Info
     */
    private boolean applyingForAid;

    private Double aidAmount;

    private String aidDocumentation;

    /*
     * Hotel Info
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

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

    private String arrivalTime; // parsing is hard :(

    @Enumerated(EnumType.STRING)
    private LuggageStorage luggageStorage;

    @Enumerated(EnumType.STRING)
    private BandColor bandColor;

    /*
     * Delegation Info
     */

    @Min(1)
    @JsonView(School.View.Summary.class)
    private Integer requestedDelegates = 1;

    @Min(1)
    private Integer requestedChaperones = 1;

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

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Address getAddress()
    {
        return address;
    }

    public void setAddress(Address address)
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

    public boolean isApplyingForAid()
    {
        return applyingForAid;
    }

    public void setApplyingForAid(boolean applyingForAid)
    {
        this.applyingForAid = applyingForAid;
    }

    public Double getAidAmount()
    {
        return aidAmount;
    }

    public void setAidAmount(Double aidAmount)
    {
        this.aidAmount = aidAmount;
    }

    public String getAidDocumentation()
    {
        return aidDocumentation;
    }

    public void setAidDocumentation(String aidDocumentation)
    {
        this.aidDocumentation = aidDocumentation;
    }

    public Hotel getHotel()
    {
        return hotel;
    }

    public void setHotel(Hotel hotel)
    {
        this.hotel = hotel;
    }

    public boolean isUsingBusunHotel()
    {
        return usingBusunHotel;
    }

    public void setUsingBusunHotel(boolean usingBusunHotel)
    {
        this.usingBusunHotel = usingBusunHotel;
    }

    public String getOtherHotel()
    {
        return otherHotel;
    }

    public void setOtherHotel(String otherHotel)
    {
        this.otherHotel = otherHotel;
    }

    public Boolean getUsingShuttle()
    {
        return usingShuttle;
    }

    public void setUsingShuttle(Boolean usingShuttle)
    {
        this.usingShuttle = usingShuttle;
    }

    public Boolean getCommuting()
    {
        return commuting;
    }

    public void setCommuting(Boolean commuting)
    {
        this.commuting = commuting;
    }

    public int getCarCount()
    {
        return carCount;
    }

    public void setCarCount(int carCount)
    {
        this.carCount = carCount;
    }

    public int getCarDays()
    {
        return carDays;
    }

    public void setCarDays(int carDays)
    {
        this.carDays = carDays;
    }

    public int getBusCount()
    {
        return busCount;
    }

    public void setBusCount(int busCount)
    {
        this.busCount = busCount;
    }

    public int getBusDays()
    {
        return busDays;
    }

    public void setBusDays(int busDays)
    {
        this.busDays = busDays;
    }

    public ShuttleService getShuttleService()
    {
        return shuttleService;
    }

    public void setShuttleService(ShuttleService shuttleService)
    {
        this.shuttleService = shuttleService;
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

    public BandColor getBandColor()
    {
        return bandColor;
    }

    public void setBandColor(BandColor bandColor)
    {
        this.bandColor = bandColor;
    }

    public Integer getRequestedDelegates()
    {
        return requestedDelegates;
    }

    public void setRequestedDelegates(Integer requestedDelegates)
    {
        this.requestedDelegates = requestedDelegates;
    }

    public Integer getRequestedChaperones()
    {
        return requestedChaperones;
    }

    public void setRequestedChaperones(Integer requestedChaperones)
    {
        this.requestedChaperones = requestedChaperones;
    }

    public Integer getParliTrainingCount()
    {
        return parliTrainingCount;
    }

    public void setParliTrainingCount(Integer parliTrainingCount)
    {
        this.parliTrainingCount = parliTrainingCount;
    }

    public Integer getCrisisTrainingCount()
    {
        return crisisTrainingCount;
    }

    public void setCrisisTrainingCount(Integer crisisTrainingCount)
    {
        this.crisisTrainingCount = crisisTrainingCount;
    }

    public Integer getTourCount()
    {
        return tourCount;
    }

    public void setTourCount(Integer tourCount)
    {
        this.tourCount = tourCount;
    }

    public Integer getInfoSessionCount()
    {
        return infoSessionCount;
    }

    public void setInfoSessionCount(Integer infoSessionCount)
    {
        this.infoSessionCount = infoSessionCount;
    }

    public String getChaperoneInfo()
    {
        return chaperoneInfo;
    }

    public void setChaperoneInfo(String chaperoneInfo)
    {
        this.chaperoneInfo = chaperoneInfo;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("address", address)
                .add("phoneNumber", phoneNumber)
                .add("applyingForAid", applyingForAid)
                .add("aidAmount", aidAmount)
                .add("aidDocumentation", aidDocumentation)
                .add("hotel", hotel)
                .add("usingBusunHotel", usingBusunHotel)
                .add("otherHotel", otherHotel)
                .add("usingShuttle", usingShuttle)
                .add("commuting", commuting)
                .add("carCount", carCount)
                .add("carDays", carDays)
                .add("busCount", busCount)
                .add("busDays", busDays)
                .add("shuttleService", shuttleService)
                .add("arrivalTime", arrivalTime)
                .add("luggageStorage", luggageStorage)
                .add("bandColor", bandColor)
                .add("requestedDelegates", requestedDelegates)
                .add("requestedChaperones", requestedChaperones)
                .add("parliTrainingCount", parliTrainingCount)
                .add("crisisTrainingCount", crisisTrainingCount)
                .add("tourCount", tourCount)
                .add("infoSessionCount", infoSessionCount)
                .add("chaperoneInfo", chaperoneInfo)
                .toString();
    }
}
