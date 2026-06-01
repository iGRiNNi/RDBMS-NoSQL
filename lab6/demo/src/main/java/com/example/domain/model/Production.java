package com.example.domain.model;

import java.time.LocalDate;
import java.util.UUID;

public class Production {

    private final UUID wellId;
    private final LocalDate productionDate;
    private final UUID id;
    private final double oil;
    private final double gas;
    private final double water;

    public Production(UUID wellId, LocalDate productionDate, UUID id, double oil, double gas, double water) {
        this.wellId = wellId;
        this.productionDate = productionDate;
        this.id = id;
        this.oil = oil;
        this.gas = gas;
        this.water = water;
    }

    public UUID getWellId() {
        return wellId;
    }

    public LocalDate getProductionDate() {
        return productionDate;
    }

    public UUID getId() {
        return id;
    }

    public double getOil() {
        return oil;
    }

    public double getGas() {
        return gas;
    }

    public double getWater() {
        return water;
    }

    @Override
    public String toString() {
        return "Production{" +
                "wellId=" + wellId +
                ", productionDate=" + productionDate +
                ", id=" + id +
                ", oil=" + oil +
                ", gas=" + gas +
                ", water=" + water +
                '}';
    }
}