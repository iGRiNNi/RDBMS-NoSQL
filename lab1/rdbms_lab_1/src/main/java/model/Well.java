package model;

public class Well {
    private long id;
    private String number;
    private String status;
    private Double depth;
    private Double diameter;
    private long clusterId;

    public Well() {
    }

    public Well(long id, String number, String status,
                Double depth, Double diameter, long clusterId) {
        this.id = id;
        this.number = number;
        this.status = status;
        this.depth = depth;
        this.diameter = diameter;
        this.clusterId = clusterId;
    }

    public Well(String number, String status,
                Double depth, Double diameter, long clusterId) {
        this.number = number;
        this.status = status;
        this.depth = depth;
        this.diameter = diameter;
        this.clusterId = clusterId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) { this.id = id; }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getDepth() {
        return depth;
    }

    public void setDepth(Double depth) {
        this.depth = depth;
    }

    public Double getDiameter() {
        return diameter;
    }

    public void setDiameter(Double diameter) {
        this.diameter = diameter;
    }

    public long getClusterId() {
        return clusterId;
    }

    public void setClusterId(long clusterId) {
        this.clusterId = clusterId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Well)) return false;
        Well well = (Well) o;
        return id != 0 && id == well.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
