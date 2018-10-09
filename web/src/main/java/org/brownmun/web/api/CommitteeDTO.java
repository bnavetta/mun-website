package org.brownmun.web.api;

import java.net.URI;

/**
 * Representation of committees to expose over the API.
 */
public class CommitteeDTO
{
    private long id;
    private String name;
    private String description;
    private String topic1;
    private String topic2;
    private String topic3;
    private String topic4;

    private Double mapLatitude;
    private Double mapLongitude;

    private URI image;

    private String email;

    private URI backgroundGuide;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTopic1() {
        return topic1;
    }

    public void setTopic1(String topic1) {
        this.topic1 = topic1;
    }

    public String getTopic2() {
        return topic2;
    }

    public void setTopic2(String topic2) {
        this.topic2 = topic2;
    }

    public String getTopic3() {
        return topic3;
    }

    public void setTopic3(String topic3) {
        this.topic3 = topic3;
    }

    public String getTopic4() {
        return topic4;
    }

    public void setTopic4(String topic4) {
        this.topic4 = topic4;
    }

    public Double getMapLatitude() {
        return mapLatitude;
    }

    public void setMapLatitude(Double mapLatitude) {
        this.mapLatitude = mapLatitude;
    }

    public Double getMapLongitude() {
        return mapLongitude;
    }

    public void setMapLongitude(Double mapLongitude) {
        this.mapLongitude = mapLongitude;
    }

    public URI getImage() {
        return image;
    }

    public void setImage(URI image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public URI getBackgroundGuide() {
        return backgroundGuide;
    }

    public void setBackgroundGuide(URI backgroundGuide) {
        this.backgroundGuide = backgroundGuide;
    }
}
