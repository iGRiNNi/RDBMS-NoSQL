package org.example.domain.model;

import java.time.LocalDate;

public class Production {

    private final Long productionId;
    private final LocalDate productionDate;
    private final Double oil;
    private final Double gas;
    private final Double water;

    public Production(Long productionId,
                      LocalDate productionDate,
                      Double oil,
                      Double gas,
                      Double water) {
        this.productionId = productionId;
        this.productionDate = productionDate;
        this.oil = oil;
        this.gas = gas;
        this.water = water;
    }

    public Long getProductionId() {
        return productionId;
    }

    public LocalDate getProductionDate() {
        return productionDate;
    }

    public Double getOil() {
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
                "productionId='" + productionId + '\'' +
                ", productionDate=" + productionDate +
                ", oil=" + oil +
                ", gas=" + gas +
                ", water=" + water +
                '}';
    }
}
