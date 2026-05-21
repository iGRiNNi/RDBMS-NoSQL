package org.example.domain.model;

public class Well {

    private final Long wellId;
    private final String number;
    private final String status;
    private final Double depth;
    private final Double diameter;

    public Well(Long wellId,
                String number,
                String status,
                Double depth,
                Double diameter) {
        this.wellId = wellId;
        this.number = number;
        this.status = status;
        this.depth = depth;
        this.diameter = diameter;
    }

    public Long getWellId() {
        return wellId;
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

    @Override
    public String toString() {
        return "Well{" +
                "wellId='" + wellId + '\'' +
                ", number='" + number + '\'' +
                ", status='" + status + '\'' +
                ", depth=" + depth +
                ", diameter=" + diameter +
                '}';
    }
}
