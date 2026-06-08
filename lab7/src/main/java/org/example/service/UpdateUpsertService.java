package org.example.service;

import org.example.dao.WellSearchDao;
import org.example.domain.document.WellDocument;

public class UpdateUpsertService {

    private final WellSearchDao wellSearchDao;

    public UpdateUpsertService(WellSearchDao wellSearchDao) {
        this.wellSearchDao = wellSearchDao;
    }

    public void run() {
        String existingId = "update-upsert-existing-well";
        String missingId = "update-upsert-missing-well";

        cleanup(existingId, missingId);

        WellDocument existingWell = new WellDocument(
                existingId,
                "bulk-field-1",
                "Самотлорское",
                "Ханты-Мансийский АО",
                "UU-101",
                "PRODUCER",
                "ACTIVE",
                3100.0,
                0.22,
                "2024-01-15",
                "Тестовая добывающая скважина для демонстрации update и upsert"
        );

        System.out.println();
        System.out.println("=== UPDATE СУЩЕСТВУЮЩЕГО ДОКУМЕНТА ===");

        wellSearchDao.create(existingWell);

        System.out.println("До update:");
        System.out.println(wellSearchDao.getById(existingId).orElse(null));

        wellSearchDao.partialUpdateStatus(existingId, "MAINTENANCE");

        System.out.println("После update:");
        System.out.println(wellSearchDao.getById(existingId).orElse(null));

        System.out.println();
        System.out.println("=== UPDATE НЕСУЩЕСТВУЮЩЕГО ДОКУМЕНТА ===");

        try {
            wellSearchDao.partialUpdateStatus(missingId, "ACTIVE");
            System.out.println("Документ обновлён");
        } catch (RuntimeException e) {
            System.out.println("Update несуществующего документа завершился ошибкой");
            System.out.println("Причина: документа с id = " + missingId + " нет");
        }

        System.out.println();
        System.out.println("Проверка документа после неудачного update:");
        System.out.println(wellSearchDao.getById(missingId).orElse(null));

        System.out.println();
        System.out.println("=== UPSERT НЕСУЩЕСТВУЮЩЕГО ДОКУМЕНТА ===");

        WellDocument upsertWell = new WellDocument(
                missingId,
                "bulk-field-2",
                "Приобское",
                "Ханты-Мансийский АО",
                "UU-202",
                "PRODUCER",
                "ACTIVE",
                3300.0,
                0.24,
                "2025-02-20",
                "Документ создан через upsert, так как до этого он отсутствовал"
        );

        wellSearchDao.upsert(upsertWell);

        System.out.println("Документ после upsert:");
        System.out.println(wellSearchDao.getById(missingId).orElse(null));

        cleanup(existingId, missingId);
    }

    private void cleanup(String existingId, String missingId) {
        wellSearchDao.delete(existingId);
        wellSearchDao.delete(missingId);
    }
}