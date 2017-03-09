package org.brownmun.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.apache.commons.lang3.text.WordUtils;

import java.text.NumberFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import javax.persistence.*;

/**
 * A finance line item like a charge or payment.
 */
@Entity
@Data
public class LineItem
{
    private static NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(Locale.US);

    @Setter(AccessLevel.NONE)
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
}
