package org.example.demo;

import org.example.dao.ProductionAnalyticsDao;
import org.example.dto.ProductionAnalyticsDto;

import java.util.List;

public final class AnalyticsDemo {

    private AnalyticsDemo() {
    }

    public static void run() {
        ProductionAnalyticsDao dao = new ProductionAnalyticsDao();

        List<ProductionAnalyticsDto> rows = dao.findTopActiveWellsByOilProduction();

        System.out.println();
        System.out.println("=== Аналитический запрос: WHERE, GROUP BY, HAVING, ORDER BY, LIMIT BY, LIMIT ===");
        System.out.println("Топ активных скважин по суммарной добыче нефти за март 2026.");
        System.out.println("(не больше 2 скважин на одно месторождение)");

        for (ProductionAnalyticsDto row : rows) {
            System.out.printf(
                    "fieldId=%d, fieldName=%s, wellId=%d, wellNumber=%s, measurements=%d, avgOil=%.2f, avgWater=%.2f, totalOil=%.2f%n",
                    row.getFieldId(),
                    row.getFieldName(),
                    row.getWellId(),
                    row.getWellNumber(),
                    row.getMeasurementsCount(),
                    row.getAvgOil(),
                    row.getAvgWater(),
                    row.getTotalOil()
            );
        }
    }
}