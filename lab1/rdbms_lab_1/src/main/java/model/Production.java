package model;

import java.time.LocalDate;

public class Production {
    private long id;
    private long wellId;
    private LocalDate date;
    private double oil;
    private Double gas;
    private Double water;

    public Production() {
    }

    public Production(long id, long wellId, LocalDate date,
                      double oil, Double gas, Double water) {
        this.id = id;
        this.wellId = wellId;
        this.date = date;
        this.oil = oil;
        this.gas = gas;
        this.water = water;
    }

    public Production(long wellId, LocalDate date,
                      double oil, Double gas, Double water) {
        this.wellId = wellId;
        this.date = date;
        this.oil = oil;
        this.gas = gas;
        this.water = water;
    }

    public long getId() {
        return id;
    }

    public long getWellId() {
        return wellId;
    }

    public void setWellId(long wellId) {
        this.wellId = wellId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getOil() {
        return oil;
    }

    public void setOil(double oil) {
        this.oil = oil;
    }

    public Double getGas() {
        return gas;
    }

    public void setGas(Double gas) {
        this.gas = gas;
    }

    public Double getWater() {
        return water;
    }

    public void setWater(Double water) {
        this.water = water;
    }

    @Override
    public String toString() {
        return "Production{" +
                "id=" + id +
                ", wellId=" + wellId +
                ", date=" + date +
                ", oil=" + oil +
                ", gas=" + gas +
                ", water=" + water +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Production)) return false;
        Production that = (Production) o;
        return id != 0 && id == that.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
