package org.example.service;

import org.example.dao.ProductionSearchDao;
import org.example.dao.WellSearchDao;
import org.example.domain.document.WellDocument;

import java.util.List;

public class FilterService {

    private final WellSearchDao wellSearchDao;
    private final ProductionSearchDao productionSearchDao;

    public FilterService(
            WellSearchDao wellSearchDao,
            ProductionSearchDao productionSearchDao
    ) {
        this.wellSearchDao = wellSearchDao;
        this.productionSearchDao = productionSearchDao;
    }

    public void run() {
        System.out.println();
        System.out.println("=== ПУНКТ 3. ИЗВЛЕЧЕНИЕ ПО УСЛОВИЮ ФИЛЬТРАЦИИ ===");

        List<WellDocument> activeDeepProducerWells =
                wellSearchDao.findActiveWellsByTypeAndMinDepth("PRODUCER", 3000.0);

        System.out.println("Активные добывающие скважины глубже 3000 м:");

        if (activeDeepProducerWells.isEmpty()) {
            System.out.println("Нет документов");
        } else {
            activeDeepProducerWells.forEach(System.out::println);
        }

        System.out.println();
        System.out.println("=== ПУНКТ 3. ОБНОВЛЕНИЕ ПО УСЛОВИЮ ФИЛЬТРАЦИИ ===");

        long updatedCount = wellSearchDao.updateStatusByTypeAndMinDepth(
                "PRODUCER",
                3000.0,
                "MAINTENANCE"
        );

        System.out.println("Обновлено документов wells: " + updatedCount);

        System.out.println();
        System.out.println("Проверка после update_by_query:");

        List<WellDocument> wellsAfterUpdate =
                wellSearchDao.findActiveWellsByTypeAndMinDepth("PRODUCER", 3000.0);

        System.out.println("Активные добывающие скважины глубже 3000 м после обновления:");

        if (wellsAfterUpdate.isEmpty()) {
            System.out.println("Нет документов");
        } else {
            wellsAfterUpdate.forEach(System.out::println);
        }

        System.out.println();
        System.out.println("=== ПУНКТ 3. УДАЛЕНИЕ ПО УСЛОВИЮ ФИЛЬТРАЦИИ ===");

        long deletedCount = productionSearchDao.deleteByOilLessThan(50.0);

        System.out.println("Удалено документов productions, где oil < 50: " + deletedCount);
    }
}