package org.example.domain.document;

public record ProductionDocument(
        String id,
        String fieldId,
        String fieldName,
        String wellId,
        String wellNumber,
        String productionDate,
        Double oil,
        Double gas,
        Double water,
        String comment
) {
}
