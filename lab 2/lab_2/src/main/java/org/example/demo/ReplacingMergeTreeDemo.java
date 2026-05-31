package org.example.demo;

import org.example.dao.ReplacingMergeTreeDao;
import org.example.domain.model.WellVersioned;

import java.util.List;

public final class ReplacingMergeTreeDemo {

    private ReplacingMergeTreeDemo() {
    }

    public static void run() {
        ReplacingMergeTreeDao dao = new ReplacingMergeTreeDao();

        dao.truncate();

        System.out.println();
        System.out.println("=== ReplacingMergeTree: вставка начальных версий ===");

        dao.insertVersion(new WellVersioned(
                1,
                "СМ-101",
                "active",
                3050.0,
                0.22,
                1L,
                1
        ));

        dao.insertVersion(new WellVersioned(
                2,
                "СМ-102",
                "active",
                3120.0,
                0.22,
                1L,
                1
        ));

        dao.insertVersion(new WellVersioned(
                3,
                "ПР-101",
                "active",
                3400.0,
                0.25,
                2L,
                1
        ));

        printRows("Все версии после начальной вставки", dao.getAllVersions());

        System.out.println();
        System.out.println("=== ReplacingMergeTree: изменение записей через INSERT новых версий ===");

        dao.insertVersion(new WellVersioned(
                1,
                "СМ-101",
                "maintenance",
                3050.0,
                0.22,
                1L,
                2
        ));

        dao.insertVersion(new WellVersioned(
                2,
                "СМ-102",
                "disabled",
                3120.0,
                0.22,
                1L,
                2
        ));

        printRows("Все версии после изменения через INSERT", dao.getAllVersions());

        printRows(
                "Последние версии по каждой скважине",
                dao.getLatestVersions()
        );

        System.out.println();
        System.out.println("=== ReplacingMergeTree: ещё одно изменение через INSERT ===");

        dao.insertVersion(new WellVersioned(
                1,
                "СМ-101",
                "active",
                3060.0,
                0.22,
                1L,
                3
        ));

        printRows("Все версии после третьей версии для id = 1", dao.getAllVersions());

        printRows(
                "Последние версии после третьей вставки",
                dao.getLatestVersions()
        );
    }

    private static void printRows(String title, List<WellVersioned> rows) {
        System.out.println();
        System.out.println("=== " + title + " ===");

        for (WellVersioned row : rows) {
            System.out.printf(
                    "id=%d, number=%s, status=%s, depth=%s, diameter=%s, fieldId=%s, version=%d%n",
                    row.getId(),
                    row.getNumber(),
                    row.getStatus(),
                    row.getDepth(),
                    row.getDiameter(),
                    row.getFieldId(),
                    row.getVersion()
            );
        }
    }
}