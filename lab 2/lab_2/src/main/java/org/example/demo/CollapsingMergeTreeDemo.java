package org.example.demo;

import org.example.dao.CollapsingMergeTreeDao;
import org.example.domain.model.ProductionCollapsing;

import java.util.List;

public final class CollapsingMergeTreeDemo {

    private CollapsingMergeTreeDemo() {
    }

    public static void run() {
        CollapsingMergeTreeDao dao = new CollapsingMergeTreeDao();

        dao.truncate();

        System.out.println();
        System.out.println("=== CollapsingMergeTree: CREATE через INSERT sign = 1 ===");

        dao.create(new ProductionCollapsing(
                1,
                1,
                120.5,
                540.0,
                18.2,
                1
        ));

        dao.create(new ProductionCollapsing(
                2,
                2,
                132.0,
                560.0,
                20.1,
                1
        ));

        printRows("Все физические строки после CREATE", dao.getAllRows());
        printRows("Актуальные записи после CREATE", dao.getAll());

        System.out.println();
        System.out.println("=== CollapsingMergeTree: READ BY ID ===");
        System.out.println("Актуальная запись с id = 1:");
        System.out.println(dao.getById(1L));

        System.out.println();
        System.out.println("=== CollapsingMergeTree: UPDATE через INSERT sign = -1 и INSERT sign = 1 ===");

        dao.update(new ProductionCollapsing(
                1,
                1,
                130.0,
                570.0,
                25.0,
                1
        ));

        printRows("Все физические строки после UPDATE id = 1", dao.getAllRows());
        printRows("Актуальные записи после UPDATE", dao.getAll());

        System.out.println();
        System.out.println("=== CollapsingMergeTree: DELETE через INSERT sign = -1 ===");

        dao.delete(2L);

        printRows("Все физические строки после DELETE id = 2", dao.getAllRows());
        printRows("Актуальные записи после DELETE", dao.getAll());

        System.out.println();
        System.out.println("=== Проверка getById после удаления ===");
        System.out.println("Актуальная запись с id = 2:");
        System.out.println(dao.getById(2L));
    }

    private static void printRows(String title, List<ProductionCollapsing> rows) {
        System.out.println();
        System.out.println("=== " + title + " ===");

        if (rows.isEmpty()) {
            System.out.println("Нет записей");
            return;
        }

        for (ProductionCollapsing row : rows) {
            System.out.printf(
                    "id=%d, wellId=%d, oil=%.2f, gas=%.2f, water=%.2f, sign=%d%n",
                    row.getId(),
                    row.getWellId(),
                    row.getOil(),
                    row.getGas(),
                    row.getWater(),
                    row.getSign()
            );
        }
    }
}