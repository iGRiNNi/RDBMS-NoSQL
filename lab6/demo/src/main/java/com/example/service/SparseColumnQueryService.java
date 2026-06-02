package com.example.service;

import com.example.dao.FieldDao;
import com.example.dao.WellDao;
import com.example.domain.model.Field;
import com.example.domain.model.Well;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class SparseColumnQueryService {

    private final FieldDao fieldDao;
    private final WellDao wellDao;

    public SparseColumnQueryService(FieldDao fieldDao, WellDao wellDao) {
        this.fieldDao = fieldDao;
        this.wellDao = wellDao;
    }

    public void run() {
        UUID fieldId = UUID.randomUUID();

        UUID producerEspId = UUID.randomUUID();
        UUID producerRodId = UUID.randomUUID();
        UUID injectorId = UUID.randomUUID();
        UUID explorationHighId = UUID.randomUUID();
        UUID explorationLowId = UUID.randomUUID();

        Field field = new Field(
                fieldId,
                "Ромашкинское",
                "Республика Татарстан",
                54.9,
                52.3,
                LocalDate.of(1948, 7, 25)
        );

        Well producerEsp = new Well(
                producerEspId,
                fieldId,
                "РМ-101",
                "ACTIVE",
                "PRODUCER",
                2800.0,
                0.18,
                "ESP",
                95.0,
                null,
                null,
                null,
                null
        );

        Well producerRod = new Well(
                producerRodId,
                fieldId,
                "РМ-102",
                "ACTIVE",
                "PRODUCER",
                2850.0,
                0.18,
                "ROD",
                80.0,
                null,
                null,
                null,
                null
        );

        Well injector = new Well(
                injectorId,
                fieldId,
                "РМ-201",
                "ACTIVE",
                "INJECTOR",
                2700.0,
                0.20,
                null,
                null,
                18.0,
                600.0,
                null,
                null
        );

        Well explorationHigh = new Well(
                explorationHighId,
                fieldId,
                "РМ-301",
                "PLANNED",
                "EXPLORATION",
                1600.0,
                0.16,
                null,
                null,
                null,
                null,
                1550.0,
                "HIGH"
        );

        Well explorationLow = new Well(
                explorationLowId,
                fieldId,
                "РМ-302",
                "PLANNED",
                "EXPLORATION",
                1500.0,
                0.16,
                null,
                null,
                null,
                null,
                1450.0,
                "LOW"
        );

        try {
            fieldDao.create(field);

            wellDao.create(producerEsp);
            wellDao.create(producerRod);
            wellDao.create(injector);
            wellDao.create(explorationHigh);
            wellDao.create(explorationLow);

            System.out.println();
            System.out.println("ЗАПРОС 1. producer_pump_type = ESP");
            System.out.println("Ищем добывающие скважины по разреженному столбцу producer_pump_type");

            printWells(wellDao.findByProducerPumpType("ESP"));

            System.out.println();
            System.out.println("ЗАПРОС 2. injector_injection_pressure >= 15");
            System.out.println("Ищем нагнетательные скважины по разреженному числовому столбцу injector_injection_pressure");

            printWells(wellDao.findByMinInjectionPressure(15.0));

            System.out.println();
            System.out.println("ЗАПРОС 3. exploration_seismic_quality = HIGH");
            System.out.println("Ищем разведочные скважины по разреженному столбцу exploration_seismic_quality");

            printWells(wellDao.findByExplorationSeismicQuality("HIGH"));

        } finally {
            wellDao.delete(producerEspId);
            wellDao.delete(producerRodId);
            wellDao.delete(injectorId);
            wellDao.delete(explorationHighId);
            wellDao.delete(explorationLowId);
            fieldDao.delete(fieldId);
        }
    }

    private void printWells(List<Well> wells) {
        if (wells.isEmpty()) {
            System.out.println("Нет записей");
            return;
        }

        for (Well well : wells) {
            System.out.println(well);
        }
    }
}