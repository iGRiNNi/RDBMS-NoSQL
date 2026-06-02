package com.example.service;

import com.example.dao.FieldDao;
import com.example.dao.WellDao;
import com.example.domain.model.Field;
import com.example.domain.model.Well;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class FilterQueryService {

    private final FieldDao fieldDao;
    private final WellDao wellDao;

    public FilterQueryService(FieldDao fieldDao, WellDao wellDao) {
        this.fieldDao = fieldDao;
        this.wellDao = wellDao;
    }

    public void run() {
        UUID fieldId = UUID.randomUUID();

        UUID wellId1 = UUID.randomUUID();
        UUID wellId2 = UUID.randomUUID();
        UUID wellId3 = UUID.randomUUID();
        UUID wellId4 = UUID.randomUUID();

        Field field = new Field(
                fieldId,
                "Приобское",
                "Ханты-Мансийский АО",
                61.0,
                73.5,
                LocalDate.of(1982, 11, 15)
        );

        Well activeDeepProducer1 = new Well(
                wellId1,
                fieldId,
                "ПР-101",
                "ACTIVE",
                "PRODUCER",
                3400.0,
                0.25,
                "ESP",
                160.0,
                null,
                null,
                null,
                null
        );

        Well activeDeepProducer2 = new Well(
                wellId2,
                fieldId,
                "ПР-102",
                "ACTIVE",
                "PRODUCER",
                3200.0,
                0.22,
                "ESP",
                140.0,
                null,
                null,
                null,
                null
        );

        Well activeShallowInjector = new Well(
                wellId3,
                fieldId,
                "ПР-201",
                "ACTIVE",
                "INJECTOR",
                2500.0,
                0.20,
                null,
                null,
                16.5,
                550.0,
                null,
                null
        );

        Well plannedExploration = new Well(
                wellId4,
                fieldId,
                "ПР-301",
                "PLANNED",
                "EXPLORATION",
                1800.0,
                0.16,
                null,
                null,
                null,
                null,
                1750.0,
                "MEDIUM"
        );

        try {
            fieldDao.create(field);

            wellDao.create(activeDeepProducer1);
            wellDao.create(activeDeepProducer2);
            wellDao.create(activeShallowInjector);
            wellDao.create(plannedExploration);

            System.out.println();
            System.out.println("ИЗВЛЕЧЕНИЕ ПО УСЛОВИЮ");
            System.out.println("Активные скважины глубже 3000 м:");

            List<Well> activeDeepWells = wellDao.findByStatusAndDepthGreaterThan(
                    "ACTIVE",
                    3000.0
            );

            printWells(activeDeepWells);

            System.out.println();
            System.out.println("ОБНОВЛЕНИЕ ПО УСЛОВИЮ");
            System.out.println("Меняем статус ACTIVE -> MAINTENANCE для скважин глубже 3000 м");

            int updatedCount = wellDao.updateStatusByStatusAndDepthGreaterThan(
                    "ACTIVE",
                    3000.0,
                    "MAINTENANCE"
            );

            System.out.println("Обновлено скважин: " + updatedCount);

            System.out.println("Проверка после обновления:");
            System.out.println(wellDao.getById(wellId1).orElse(null));
            System.out.println(wellDao.getById(wellId2).orElse(null));
            System.out.println(wellDao.getById(wellId3).orElse(null));

            System.out.println();
            System.out.println("УДАЛЕНИЕ ПО УСЛОВИЮ");
            System.out.println("Удаляем скважины типа EXPLORATION со статусом PLANNED");

            int deletedCount = wellDao.deleteByTypeAndStatus(
                    "EXPLORATION",
                    "PLANNED"
            );

            System.out.println("Удалено скважин: " + deletedCount);

            System.out.println("Проверка удалённой скважины:");
            System.out.println(wellDao.getById(wellId4).orElse(null));

        } finally {
            wellDao.delete(wellId1);
            wellDao.delete(wellId2);
            wellDao.delete(wellId3);
            wellDao.delete(wellId4);
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