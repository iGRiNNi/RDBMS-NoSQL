import dal.dao.ClusterDao;
import dal.dao.FieldDao;
import dal.dao.WellDao;
import dal.infrastructure.connection.ConnectionManager;
import dal.infrastructure.connection.DatabaseConfig;
import dal.infrastructure.migration.MigrationRunner;
import dto.ClusterHierarchyDto;
import dto.FieldContractorDto;
import dto.WellAverageComparisonDto;
import dto.WellWaterOilDto;
import model.Well;

import java.sql.Connection;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DatabaseConfig config = DatabaseConfig.load();

        MigrationRunner migrationRunner = new MigrationRunner(config);
        migrationRunner.migrate();

        var fieldDao = new FieldDao();

        showJoinTasks(fieldDao);

        var wellDao = new WellDao();

        showWateredWellsTask(wellDao);

        showWellAverageComparisonTask(wellDao);

        var clusterDao = new ClusterDao();

        showClusterHierarchyTask(clusterDao, 1);
    }

    private static void showJoinTasks(FieldDao fieldDao) {
        printJoinResult("INNER JOIN", fieldDao.getFieldsWithContractorsInnerJoin());
        printJoinResult("LEFT JOIN", fieldDao.getFieldsWithContractorsLeftJoin());
        printJoinResult("RIGHT JOIN", fieldDao.getFieldsWithContractorsRightJoin());
        printJoinResult("FULL JOIN", fieldDao.getFieldsWithContractorsFullJoin());
        printJoinResult("CROSS JOIN", fieldDao.getFieldsWithContractorsCrossJoin());
    }

    private static void printJoinResult(String title, List<FieldContractorDto> result) {
        System.out.println();
        System.out.println("=== " + title + " ===");

        if (result.isEmpty()) {
            System.out.println("Нет данных");
            return;
        }

        for (FieldContractorDto dto : result) {
            System.out.println(
                    "Месторождение: " + safe(dto.getFieldName()) +
                            ", регион: " + safe(dto.getRegion()) +
                            ", подрядчик: " + safe(dto.getContractorName())
            );
        }
    }

    private static String safe(String value) {
        return value == null ? "-" : value;
    }

    private static void showWateredWellsTask(WellDao wellDao) {
        System.out.println();
        System.out.println("=== Скважины с повышенным средним объёмом воды за последний месяц ===");

        List<WellWaterOilDto> result = wellDao.getTopWateredActiveWellsLastMonth();

        if (result.isEmpty()) {
            System.out.println("Нет данных");
            return;
        }

        for (WellWaterOilDto dto : result) {
            System.out.println(
                    "Группа: " + dto.getGroupName() +
                            ", скважина: " + dto.getWellNumber() +
                            ", средняя нефть: " + dto.getAvgOil() +
                            ", средняя вода: " + dto.getAvgWater()
            );
        }
    }

    private static void showWellAverageComparisonTask(WellDao wellDao) {
        System.out.println();
        System.out.println("=== Сравнение средней добычи скважины со средней добычей в группе ===");

        List<WellAverageComparisonDto> result = wellDao.compareWellAverageOilWithGroupAverage();

        if (result.isEmpty()) {
            System.out.println("Нет данных");
            return;
        }

        for (WellAverageComparisonDto dto : result) {
            System.out.println(
                    "Группа: " + dto.getGroupName()
                            + ", скважина: " + dto.getWellNumber()
                            + ", средняя добыча скважины: " + dto.getAvgOilByWell()
                            + ", средняя по группе: " + dto.getAvgOilInGroup()
                            + ", результат: " + dto.getComparisonResult()
            );
        }
    }

    private static void showClusterHierarchyTask(ClusterDao clusterDao, long fieldId) {
        System.out.println();
        System.out.println("=== Иерархия групп месторождения ===");

        List<ClusterHierarchyDto> result = clusterDao.getClusterHierarchyByFieldId(fieldId);

        if (result.isEmpty()) {
            System.out.println("Нет данных");
            return;
        }

        for (ClusterHierarchyDto dto : result) {
            String indent = "  ".repeat(dto.getLevel() - 1);

            System.out.println(
                    indent
                            + "- "
                            + dto.getName()
                            + " (id=" + dto.getId()
                            + ", уровень=" + dto.getLevel()
                            + ")"
            );
        }
    }
}
