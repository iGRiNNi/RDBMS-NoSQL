package org.example.domain.model;

public class Field {

    private final Long fieldId;
    private final String name;
    private final String region;
    private final Double latitude;
    private final Double longitude;
    private final Double oilReserves;

    public Field(Long fieldId,
                 String name,
                 String region,
                 Double latitude,
                 Double longitude,
                 Double oilReserves) {
        this.fieldId = fieldId;
        this.name = name;
        this.region = region;
        this.latitude = latitude;
        this.longitude = longitude;
        this.oilReserves = oilReserves;
    }

    public Long getFieldId() {
        return fieldId;
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

    public Double getOilReserves() {
        return oilReserves;
    }

    @Override
    public String toString() {
        return "Field{" +
                "fieldId='" + fieldId + '\'' +
                ", name='" + name + '\'' +
                ", region='" + region + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", oilReserves=" + oilReserves +
                '}';
    }
}
