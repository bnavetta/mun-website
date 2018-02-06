package org.brownmun.model;

import org.apache.commons.lang3.text.WordUtils;

import javax.persistence.*;
import java.text.NumberFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;

/**
 * A finance line item like a charge or payment.
 */
@Entity
public class LineItem
{
    private static NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(Locale.US);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    /**
     * How much (in USD) the item is for
     */
    private double amount;

    /**
     * A descriptive name for the item
     */
    private String name;

    /**
     * Space for more details
     */
    private String memo;

    /**
     * When the item was created
     */
    private Instant timestamp;

    @Transient
    public String formattedAmount()
    {
        return CURRENCY_FORMATTER.format(amount);
    }

    @Transient
    public Date getTimestampAsDate()
    {
        return Date.from(timestamp);
    }

    @Enumerated(EnumType.ORDINAL)
    private Type type;

    public enum Type
    {
        CHARGE,
        AID_AWARD,
        PAYMENT;

        @Override
        public String toString()
        {
            return WordUtils.capitalizeFully(name().replace('_', ' '));
        }
    }

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

    public double getAmount()
    {
        return amount;
    }

    public void setAmount(double amount)
    {
        this.amount = amount;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getMemo()
    {
        return memo;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    public Instant getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp)
    {
        this.timestamp = timestamp;
    }

    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }
}
