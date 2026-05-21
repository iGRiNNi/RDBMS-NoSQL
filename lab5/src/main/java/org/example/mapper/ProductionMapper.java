package org.example.mapper;

import org.example.domain.model.Production;
import org.neo4j.driver.types.Node;

import java.time.LocalDate;

public final class ProductionMapper {

    private ProductionMapper() {
    }

    public static Production fromNode(Node node) {
        return new Production(
                node.get("productionId").asLong(),
                LocalDate.parse(node.get("productionDate").asString()),
                node.get("oil").isNull() ? null : node.get("oil").asDouble(),
                node.get("gas").isNull() ? null : node.get("gas").asDouble(),
                node.get("water").isNull() ? null : node.get("water").asDouble()
        );
    }
}
