package com.example.domain.key;

import java.time.LocalDate;
import java.util.UUID;

public class ProductionKey {

    private final UUID wellId;
    private final LocalDate productionDate;
    private final UUID id;

    public ProductionKey(UUID wellId, LocalDate productionDate, UUID id) {
        this.wellId = wellId;
        this.productionDate = productionDate;
        this.id = id;
    }

    public UUID getWellId() {
        return wellId;
    }

    public LocalDate getProductionDate() {
        return productionDate;
    }

    public UUID getId() {
        return id;
    }
}