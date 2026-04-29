package org.example.domain.model;

public class Field {

    private long id;
    private String name;
    private String region;
    private Double latitude;
    private Double longitude;

    public Field(long id, String name, String region, Double latitude, Double longitude) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "Field{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", region='" + region + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
