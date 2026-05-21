package org.example.domain.relationship;

import java.time.LocalDate;

public class FieldWellRelation {

    private final LocalDate assignedAt;
    private final String relationStatus;
    private final String comment;

    public FieldWellRelation(LocalDate assignedAt,
                             String relationStatus,
                             String comment) {
        this.assignedAt = assignedAt;
        this.relationStatus = relationStatus;
        this.comment = comment;
    }

    public LocalDate getAssignedAt() {
        return assignedAt;
    }

    public String getRelationStatus() {
        return relationStatus;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "FieldWellRelation{" +
                "assignedAt=" + assignedAt +
                ", relationStatus='" + relationStatus + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
