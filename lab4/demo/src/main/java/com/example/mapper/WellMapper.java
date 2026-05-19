package com.example.mapper;

import org.bson.Document;
import com.example.domain.model.Well;

public final class WellMapper {

    private WellMapper() {
    }

    public static Document toDocument(Well well) {
        Document document = new Document();

        document.append("_id", well.getId());
        document.append("number", well.getNumber());
        document.append("status", well.getStatus());
        document.append("depth", well.getDepth());
        document.append("diameter", well.getDiameter());
        document.append("fieldId", well.getFieldId());

        return document;
    }

    public static Well fromDocument(Document document) {
        if (document == null) {
            return null;
        }

        return new Well(
                document.getLong("_id"),
                document.getString("number"),
                document.getString("status"),
                getNullableDouble(document, "depth"),
                getNullableDouble(document, "diameter"),
                document.getLong("fieldId")
        );
    }

    private static Double getNullableDouble(Document document, String key) {
        Number value = document.get(key, Number.class);
        return value == null ? null : value.doubleValue();
    }
}
