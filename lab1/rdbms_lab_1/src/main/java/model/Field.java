package model;

import java.time.LocalDate;

public class Field {

    private long id;
    private String name;
    private String region;
    private Double latitude;
    private Double longitude;
    private LocalDate startDate;
    private Double oilReserves;

    public Field() {
    }

    public Field(long id, String name, String region,
                 Double latitude, Double longitude,
                 LocalDate startDate, Double oilReserves) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.latitude = latitude;
        this.longitude = longitude;
        this.startDate = startDate;
        this.oilReserves = oilReserves;
    }

    public Field(String name, String region,
                 Double latitude, Double longitude,
                 LocalDate startDate, Double oilReserves) {
        this.name = name;
        this.region = region;
        this.latitude = latitude;
        this.longitude = longitude;
        this.startDate = startDate;
        this.oilReserves = oilReserves;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Double getOilReserves() {
        return oilReserves;
    }

    public void setOilReserves(Double oilReserves) {
        this.oilReserves = oilReserves;
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
                ", oilReserves=" + oilReserves +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Field)) return false;
        Field field = (Field) o;
        return id != 0 && id == field.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}