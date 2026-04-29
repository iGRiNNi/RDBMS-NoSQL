package dto;

public class FieldContractorDto {

    private final String fieldName;
    private final String region;
    private final String contractorName;

    public FieldContractorDto(String fieldName, String region, String contractorName) {
        this.fieldName = fieldName;
        this.region = region;
        this.contractorName = contractorName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getRegion() {
        return region;
    }

    public String getContractorName() {
        return contractorName;
    }

    @Override
    public String toString() {
        return "FieldContractorDto{" +
                "fieldName='" + fieldName + '\'' +
                ", region='" + region + '\'' +
                ", contractorName='" + contractorName + '\'' +
                '}';
    }
}