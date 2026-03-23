package model;

public class Cluster {
    private long id;
    private String name;
    private long fieldId;
    private Long parentClusterId;

    public Cluster() {
    }

    public Cluster(long id, String name, long fieldId, Long parentClusterId) {
        this.id = id;
        this.name = name;
        this.fieldId = fieldId;
        this.parentClusterId = parentClusterId;
    }

    public Cluster(String name, long fieldId, Long parentClusterId) {
        this.name = name;
        this.fieldId = fieldId;
        this.parentClusterId = parentClusterId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getFieldId() {
        return fieldId;
    }

    public void setFieldId(long fieldId) {
        this.fieldId = fieldId;
    }

    public Long getParentClusterId() {
        return parentClusterId;
    }

    public void setParentClusterId(Long parentClusterId) {
        this.parentClusterId = parentClusterId;
    }

    @Override
    public String toString() {
        return "Cluster{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fieldId=" + fieldId +
                ", parentClusterId=" + parentClusterId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cluster)) return false;
        Cluster cluster = (Cluster) o;
        return id != 0 && id == cluster.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
