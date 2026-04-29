package org.example.domain.model;

public class Well {

    private long id;
    private String number;
    private String status;
    private Double depth;
    private Double diameter;
    private long clusterId;

    public Well(long id, String number, String status,
                Double depth, Double diameter, long clusterId) {
        this.id = id;
        this.number = number;
        this.status = status;
        this.depth = depth;
        this.diameter = diameter;
        this.clusterId = clusterId;
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

    public long getClusterId() {
        return clusterId;
    }

    @Override
    public String toString() {
        return "Well{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", status='" + status + '\'' +
                ", depth=" + depth +
                ", diameter=" + diameter +
                ", clusterId=" + clusterId +
                '}';
    }
}
