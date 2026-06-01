package com.example.domain.model;

import java.time.LocalDate;
import java.util.UUID;

public class Field {

    private final UUID id;
    private final String name;
    private final String region;
    private final Double latitude;
    private final Double longitude;
    private final LocalDate startDate;

    public Field(UUID id, String name, String region, Double latitude, Double longitude, LocalDate startDate) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.latitude = latitude;
        this.longitude = longitude;
        this.startDate = startDate;
    }

    public UUID getId() {
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

    public LocalDate getStartDate() {
        return startDate;
    }

    @Override
    public String toString() {
        return "Field{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", region='" + region + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", startDate=" + startDate +
                '}';
    }
}