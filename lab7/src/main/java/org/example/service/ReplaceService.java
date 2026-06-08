package org.example.service;

import org.example.dao.WellSearchDao;
import org.example.domain.document.WellDocument;

import java.util.Map;

public class ReplaceService {

    private final WellSearchDao wellSearchDao;

    public ReplaceService(WellSearchDao wellSearchDao) {
        this.wellSearchDao = wellSearchDao;
    }

    public void run() {
        String wellId = "replace-demo-well";

        cleanup(wellId);

        WellDocument well = new WellDocument(
                wellId,
                "bulk-field-1",
                "Самотлорское",
                "Ханты-Мансийский АО",
                "RP-101",
                "PRODUCER",
                "ACTIVE",
                3150.0,
                0.22,
                "2024-04-10",
                "Добывающая скважина с электроцентробежным насосом и стабильной добычей нефти"
        );

        System.out.println();
        System.out.println("=== ИСХОДНЫЙ ДОКУМЕНТ ===");

        wellSearchDao.create(well);
        System.out.println(wellSearchDao.getById(wellId).orElse(null));

        System.out.println();
        System.out.println("=== ЧАСТИЧНОЕ ОБНОВЛЕНИЕ ЧЕРЕЗ UPDATE API ===");
        System.out.println("Меняем только status и description. Остальные поля должны остаться.");

        wellSearchDao.partialUpdate(
                wellId,
                Map.of(
                        "status", "MAINTENANCE",
                        "description", "Скважина переведена в обслуживание после диагностики"
                )
        );

        System.out.println("Документ после partial update:");
        System.out.println(wellSearchDao.getById(wellId).orElse(null));

        System.out.println();
        System.out.println("=== ПОЛНАЯ ЗАМЕНА ЧЕРЕЗ INDEX API С ТЕМ ЖЕ _id ===");
        System.out.println("Передаём новый документ без части старых полей. Старые поля исчезнут из _source.");

        wellSearchDao.replaceDocument(
                wellId,
                Map.of(
                        "id", wellId,
                        "fieldId", "bulk-field-1",
                        "fieldName", "Самотлорское",
                        "number", "RP-101",
                        "status", "DISABLED"
                )
        );

        System.out.println("Документ после replace:");
        System.out.println(wellSearchDao.getById(wellId).orElse(null));

        cleanup(wellId);
    }

    private void cleanup(String wellId) {
        wellSearchDao.delete(wellId);
    }
}