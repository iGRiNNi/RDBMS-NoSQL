package org.example.domain.document;

public record FieldDocument(
        String id,
        String name,
        String region,
        Double latitude,
        Double longitude,
        String startDate,
        String description
) {
}