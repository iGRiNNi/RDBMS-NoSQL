package org.example.dto;

public class ProductionAnalyticsDto {

    private final long fieldId;
    private final String fieldName;
    private final long wellId;
    private final String wellNumber;
    private final long measurementsCount;
    private final double avgOil;
    private final double avgWater;
    private final double totalOil;

    public ProductionAnalyticsDto(
            long fieldId,
            String fieldName,
            long wellId,
            String wellNumber,
            long measurementsCount,
            double avgOil,
            double avgWater,
            double totalOil
    ) {
        this.fieldId = fieldId;
        this.fieldName = fieldName;
        this.wellId = wellId;
        this.wellNumber = wellNumber;
        this.measurementsCount = measurementsCount;
        this.avgOil = avgOil;
        this.avgWater = avgWater;
        this.totalOil = totalOil;
    }

    public long getFieldId() {
        return fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public long getWellId() {
        return wellId;
    }

    public String getWellNumber() {
        return wellNumber;
    }

    public long getMeasurementsCount() {
        return measurementsCount;
    }

    public double getAvgOil() {
        return avgOil;
    }

    public double getAvgWater() {
        return avgWater;
    }

    public double getTotalOil() {
        return totalOil;
    }
}