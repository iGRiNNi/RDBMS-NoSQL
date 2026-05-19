package com.example.domain.model;

import org.bson.types.ObjectId;

public class Field {

    private final ObjectId id;
    private final String name;
    private final String region;
    private final Double latitude;
    private final Double longitude;

    public Field(ObjectId id, String name, String region, Double latitude, Double longitude) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public ObjectId getId() {
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
