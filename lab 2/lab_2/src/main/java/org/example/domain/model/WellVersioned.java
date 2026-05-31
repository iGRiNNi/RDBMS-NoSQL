package org.example.domain.model;

public class WellVersioned {

    private final long id;
    private final String number;
    private final String status;
    private final Double depth;
    private final Double diameter;
    private final Long fieldId;
    private final long version;

    public WellVersioned(
            long id,
            String number,
            String status,
            Double depth,
            Double diameter,
            Long fieldId,
            long version
    ) {
        this.id = id;
        this.number = number;
        this.status = status;
        this.depth = depth;
        this.diameter = diameter;
        this.fieldId = fieldId;
        this.version = version;
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

    public long getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "WellVersioned{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", status='" + status + '\'' +
                ", depth=" + depth +
                ", diameter=" + diameter +
                ", fieldId=" + fieldId +
                ", version=" + version +
                '}';
    }
}
