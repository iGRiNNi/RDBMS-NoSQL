package org.example.domain.model;

import java.time.LocalDate;

public class Production {

    private long id;
    private long wellId;
    private LocalDate productionDate;
    private double oil;
    private Double gas;
    private Double water;

    public Production(long id, long wellId, LocalDate productionDate, double oil, Double gas, Double water) {
        this.id = id;
        this.wellId = wellId;
        this.productionDate = productionDate;
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

    public LocalDate getProductionDate() {
        return productionDate;
    }

    public double getOil() {
        return oil;
    }

    public Double getGas() {
        return gas;
    }

    public Double getWater() {
        return water;
    }

    public void setWellId(long wellId) {
        this.wellId = wellId;
    }

    public void setProductionDate(LocalDate productionDate) {
        this.productionDate = productionDate;
    }

    public void setOil(double oil) {
        this.oil = oil;
    }

    public void setGas(Double gas) {
        this.gas = gas;
    }

    public void setWater(Double water) {
        this.water = water;
    }

    @Override
    public String toString() {
        return "Production{" +
                "id=" + id +
                ", wellId=" + wellId +
                ", productionDate=" + productionDate +
                ", oil=" + oil +
                ", gas=" + gas +
                ", water=" + water +
                '}';
    }
}