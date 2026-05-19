package com.example.mapper;

import org.bson.Document;
import com.example.domain.model.Production;

import java.time.LocalDate;

public final class ProductionMapper {

    private ProductionMapper() {
    }

    public static Document toDocument(Production production) {
        Document document = new Document();

        document.append("_id", production.getId());
        document.append("wellId", production.getWellId());
        document.append("productionDate", production.getProductionDate().toString());
        document.append("oil", production.getOil());
        document.append("gas", production.getGas());
        document.append("water", production.getWater());

        return document;
    }

    public static Production fromDocument(Document document) {
        if (document == null) {
            return null;
        }

        return new Production(
                document.getLong("_id"),
                document.getLong("wellId"),
                LocalDate.parse(document.getString("productionDate")),
                document.getDouble("oil"),
                getNullableDouble(document, "gas"),
                getNullableDouble(document, "water")
        );
    }

    private static Double getNullableDouble(Document document, String key) {
        Number value = document.get(key, Number.class);
        return value == null ? null : value.doubleValue();
    }
}