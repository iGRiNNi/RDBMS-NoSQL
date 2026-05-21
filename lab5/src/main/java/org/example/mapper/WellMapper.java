package org.example.mapper;

import org.example.domain.model.Well;
import org.neo4j.driver.types.Node;

public final class WellMapper {

    private WellMapper() {
    }

    public static Well fromNode(Node node) {
        return new Well(
                node.get("wellId").asLong(),
                node.get("number").asString(null),
                node.get("status").asString(null),
                node.get("depth").isNull() ? null : node.get("depth").asDouble(),
                node.get("diameter").isNull() ? null : node.get("diameter").asDouble()
        );
    }
}
