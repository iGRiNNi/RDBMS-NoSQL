package com.example.domain.model;

import java.time.LocalDate;

public class Production {

    private final long id;
    private final long wellId;
    private final LocalDate productionDate;
    private final double oil;
    private final Double gas;
    private final Double water;

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
