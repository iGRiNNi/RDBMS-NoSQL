package dto;

public class WellAverageComparisonDto {
    private final String groupName;
    private final String wellNumber;
    private final double avgOilByWell;
    private final double avgOilInGroup;
    private final String comparisonResult;

    public WellAverageComparisonDto(
            String groupName,
            String wellNumber,
            double avgOilByWell,
            double avgOilInGroup,
            String comparisonResult
    ) {
        this.groupName = groupName;
        this.wellNumber = wellNumber;
        this.avgOilByWell = avgOilByWell;
        this.avgOilInGroup = avgOilInGroup;
        this.comparisonResult = comparisonResult;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getWellNumber() {
        return wellNumber;
    }

    public double getAvgOilByWell() {
        return avgOilByWell;
    }

    public double getAvgOilInGroup() {
        return avgOilInGroup;
    }

    public String getComparisonResult() {
        return comparisonResult;
    }
}
