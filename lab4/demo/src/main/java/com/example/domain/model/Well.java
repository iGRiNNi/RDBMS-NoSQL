package com.example.domain.model;

import org.bson.types.ObjectId;

public class Well {

    private final ObjectId id;
    private final String number;
    private final String status;
    private final Double depth;
    private final Double diameter;
    private final ObjectId fieldId;

    public Well(ObjectId id, String number, String status,
                Double depth, Double diameter, ObjectId fieldId) {
        this.id = id;
        this.number = number;
        this.status = status;
        this.depth = depth;
        this.diameter = diameter;
        this.fieldId = fieldId;
    }

    public ObjectId getId() {
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

    public ObjectId getFieldId() {
        return fieldId;
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