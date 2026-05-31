package org.example.domain.model;

public class ProductionCollapsing {

    private final long id;
    private final long wellId;
    private final double oil;
    private final double gas;
    private final double water;
    private final int sign;

    public ProductionCollapsing(
            long id,
            long wellId,
            double oil,
            double gas,
            double water,
            int sign
    ) {
        this.id = id;
        this.wellId = wellId;
        this.oil = oil;
        this.gas = gas;
        this.water = water;
        this.sign = sign;
    }

    public long getId() {
        return id;
    }

    public long getWellId() {
        return wellId;
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

    public int getSign() {
        return sign;
    }

    @Override
    public String toString() {
        return "ProductionCollapsing{" +
                "id=" + id +
                ", wellId=" + wellId +
                ", oil=" + oil +
                ", gas=" + gas +
                ", water=" + water +
                ", sign=" + sign +
                '}';
    }
}