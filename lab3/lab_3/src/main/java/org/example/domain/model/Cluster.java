package org.example.domain.model;

public class Cluster {

    private long id;
    private String name;
    private long fieldId;
    private Long parentClusterId;

    public Cluster(long id, String name, long fieldId, Long parentClusterId) {
        this.id = id;
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

    public long getFieldId() {
        return fieldId;
    }

    public Long getParentClusterId() {
        return parentClusterId;
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
}