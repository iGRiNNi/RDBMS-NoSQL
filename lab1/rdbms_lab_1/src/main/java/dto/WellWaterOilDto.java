package dto;

public class WellWaterOilDto {
    private final String groupName;
    private final String wellNumber;
    private final double avgOil;
    private final double avgWater;

    public WellWaterOilDto(String groupName, String wellNumber, double avgOil, double avgWater) {
        this.groupName = groupName;
        this.wellNumber = wellNumber;
        this.avgOil = avgOil;
        this.avgWater = avgWater;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getWellNumber() {
        return wellNumber;
    }

    public double getAvgOil() {
        return avgOil;
    }

    public double getAvgWater() {
        return avgWater;
    }
}
