package org.example.domain.model;

public class Well {

    private long id;
    private String number;
    private String status;
    private Double depth;
    private Double diameter;
    private Long fieldId;

    public Well(long id, String number, String status, Double depth, Double diameter, Long fieldId) {
        this.id = id;
        this.number = number;
        this.status = status;
        this.depth = depth;
        this.diameter = diameter;
        this.fieldId = fieldId;
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getStatus() {
        return status;
    }

    public Double getDepth() {
        return depth;
    }

    public Double getDiameter() {
        return diameter;
    }

    public Long getFieldId() {
        return fieldId;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDepth(Double depth) {
        this.depth = depth;
    }

    public void setDiameter(Double diameter) {
        this.diameter = diameter;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    @Override
    public String toString() {
        return "Well{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", status='" + status + '\'' +
                ", depth=" + depth +
                ", diameter=" + diameter +
                ", fieldId=" + fieldId +
                '}';
    }
}