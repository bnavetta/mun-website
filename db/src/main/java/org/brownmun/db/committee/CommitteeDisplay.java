package org.brownmun.db.committee;

import com.google.common.base.MoreObjects;

import java.net.URI;
import java.util.Objects;

/**
 * Representation of a {@link Committee} for displaying.
 */
public class CommitteeDisplay
{
    private final Long id;

    private final String name;

    private final CommitteeType type;

    private final String description;

    private final Float latitude;
    private final Float longitude;

    private final URI image;

    public CommitteeDisplay(Long id, String name, CommitteeType type, String description, Float latitude, Float longitude, URI image)
    {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
    }

    public Long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public CommitteeType getType()
    {
        return type;
    }

    public String getDescription()
    {
        return description;
    }

    public Float getLatitude()
    {
        return latitude;
    }

    public Float getLongitude()
    {
        return longitude;
    }

    public URI getImage()
    {
        return image;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommitteeDisplay that = (CommitteeDisplay) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(latitude, that.latitude) &&
                Objects.equals(longitude, that.longitude) &&
                Objects.equals(image, that.image);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, name, description, type, latitude, longitude, image);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("type", type)
                .add("description", description)
                .add("latitude", latitude)
                .add("longitude", longitude)
                .add("image", image)
                .toString();
    }
}
