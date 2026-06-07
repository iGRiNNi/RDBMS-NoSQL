package org.example.domain.document;

public record WellDocument(
        String id,
        String fieldId,
        String fieldName,
        String region,
        String number,
        String type,
        String status,
        Double depth,
        Double diameter,
        String commissioningDate,
        String description
) {
}
