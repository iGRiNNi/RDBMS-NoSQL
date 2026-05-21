package org.example.mapper;

import org.example.domain.model.Field;
import org.neo4j.driver.types.Node;

public final class FieldMapper {

    private FieldMapper() {
    }

    public static Field fromNode(Node node) {
        return new Field(
                node.get("fieldId").asLong(),
                node.get("name").asString(null),
                node.get("region").asString(null),
                node.get("latitude").isNull() ? null : node.get("latitude").asDouble(),
                node.get("longitude").isNull() ? null : node.get("longitude").asDouble(),
                node.get("oilReserves").isNull() ? null : node.get("oilReserves").asDouble()
        );
    }
}
