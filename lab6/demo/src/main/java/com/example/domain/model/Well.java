package com.example.domain.model;

import java.util.UUID;

public class Well {

    private final UUID id;
    private final UUID fieldId;
    private final String number;
    private final String status;
    private final String wellType;
    private final Double depth;
    private final Double diameter;

    private final String producerPumpType;
    private final Double producerTargetOil;

    private final Double injectorInjectionPressure;
    private final Double injectorTargetWater;

    private final Double explorationCoreDepth;
    private final String explorationSeismicQuality;

    public Well(
            UUID id,
            UUID fieldId,
            String number,
            String status,
            String wellType,
            Double depth,
            Double diameter,
            String producerPumpType,
            Double producerTargetOil,
            Double injectorInjectionPressure,
            Double injectorTargetWater,
            Double explorationCoreDepth,
            String explorationSeismicQuality
    ) {
        this.id = id;
        this.fieldId = fieldId;
        this.number = number;
        this.status = status;
        this.wellType = wellType;
        this.depth = depth;
        this.diameter = diameter;
        this.producerPumpType = producerPumpType;
        this.producerTargetOil = producerTargetOil;
        this.injectorInjectionPressure = injectorInjectionPressure;
        this.injectorTargetWater = injectorTargetWater;
        this.explorationCoreDepth = explorationCoreDepth;
        this.explorationSeismicQuality = explorationSeismicQuality;
    }

    public UUID getId() {
        return id;
    }

    public UUID getFieldId() {
        return fieldId;
    }

    public String getNumber() {
        return number;
    }

    public String getStatus() {
        return status;
    }

    public String getWellType() {
        return wellType;
    }

    public Double getDepth() {
        return depth;
    }

    public Double getDiameter() {
        return diameter;
    }

    public String getProducerPumpType() {
        return producerPumpType;
    }

    public Double getProducerTargetOil() {
        return producerTargetOil;
    }

    public Double getInjectorInjectionPressure() {
        return injectorInjectionPressure;
    }

    public Double getInjectorTargetWater() {
        return injectorTargetWater;
    }

    public Double getExplorationCoreDepth() {
        return explorationCoreDepth;
    }

    public String getExplorationSeismicQuality() {
        return explorationSeismicQuality;
    }

    @Override
    public String toString() {
        return "Well{" +
                "id=" + id +
                ", fieldId=" + fieldId +
                ", number='" + number + '\'' +
                ", status='" + status + '\'' +
                ", wellType='" + wellType + '\'' +
                ", depth=" + depth +
                ", diameter=" + diameter +
                ", producerPumpType='" + producerPumpType + '\'' +
                ", producerTargetOil=" + producerTargetOil +
                ", injectorInjectionPressure=" + injectorInjectionPressure +
                ", injectorTargetWater=" + injectorTargetWater +
                ", explorationCoreDepth=" + explorationCoreDepth +
                ", explorationSeismicQuality='" + explorationSeismicQuality + '\'' +
                '}';
    }
}