package org.brownmun.db.committee;

import com.google.common.base.MoreObjects;

import javax.persistence.*;
import java.net.URI;

/**
 * Information about a committee used only for display.
 */
@Entity
@Table(name = "committee_display_info")
public class DisplayInfo
{
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Committee committee;

    /**
     * The longitude and latitude are the coordinates to use when displaying the committee on a map.
     */

    private Float longitude;
    private Float latitude;

    /**
     * A cover image to display with the committee.
     */
    private URI image;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Float getLongitude()
    {
        return longitude;
    }

    public void setLongitude(Float longitude)
    {
        this.longitude = longitude;
    }

    public Float getLatitude()
    {
        return latitude;
    }

    public void setLatitude(Float latitude)
    {
        this.latitude = latitude;
    }

    public URI getImage()
    {
        return image;
    }

    public void setImage(URI image)
    {
        this.image = image;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("committee", committee)
                .add("longitude", longitude)
                .add("latitude", latitude)
                .add("image", image)
                .toString();
    }
}
