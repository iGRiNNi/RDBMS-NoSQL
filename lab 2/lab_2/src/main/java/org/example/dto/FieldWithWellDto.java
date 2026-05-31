package org.example.dto;

public class FieldWithWellDto {

    private final Long fieldId;
    private final String fieldName;
    private final Long wellId;
    private final String wellNumber;
    private final String wellStatus;

    public FieldWithWellDto(Long fieldId, String fieldName, Long wellId, String wellNumber, String wellStatus) {
        this.fieldId = fieldId;
        this.fieldName = fieldName;
        this.wellId = wellId;
        this.wellNumber = wellNumber;
        this.wellStatus = wellStatus;
    }

    public Long getFieldId() {
        return fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Long getWellId() {
        return wellId;
    }

    public String getWellNumber() {
        return wellNumber;
    }

    public String getWellStatus() {
        return wellStatus;
    }

    @Override
    public String toString() {
        return "FieldWellJoinDto{" +
                "fieldId=" + fieldId +
                ", fieldName='" + fieldName + '\'' +
                ", wellId=" + wellId +
                ", wellNumber='" + wellNumber + '\'' +
                ", wellStatus='" + wellStatus + '\'' +
                '}';
    }
}
