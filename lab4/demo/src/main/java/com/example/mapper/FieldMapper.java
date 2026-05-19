package com.example.mapper;

import org.bson.Document;
import com.example.domain.model.Field;

public final class FieldMapper {

    private FieldMapper() {
    }

    public static Document toDocument(Field field) {
        Document document = new Document();

        document.append("_id", field.getId());
        document.append("name", field.getName());
        document.append("region", field.getRegion());
        document.append("latitude", field.getLatitude());
        document.append("longitude", field.getLongitude());

        return document;
    }

    public static Field fromDocument(Document document) {
        if (document == null) {
            return null;
        }

        return new Field(
                document.getLong("_id"),
                document.getString("name"),
                document.getString("region"),
                getNullableDouble(document, "latitude"),
                getNullableDouble(document, "longitude")
        );
    }

    private static Double getNullableDouble(Document document, String key) {
        Number value = document.get(key, Number.class);
        return value == null ? null : value.doubleValue();
    }
}
