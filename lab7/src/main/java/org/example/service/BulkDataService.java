package org.example.service;

import org.example.dao.FieldSearchDao;
import org.example.dao.ProductionSearchDao;
import org.example.dao.WellSearchDao;
import org.example.domain.document.FieldDocument;
import org.example.domain.document.ProductionDocument;
import org.example.domain.document.WellDocument;

import java.util.List;

public class BulkDataService {

    private final FieldSearchDao fieldSearchDao;
    private final WellSearchDao wellSearchDao;
    private final ProductionSearchDao productionSearchDao;

    public BulkDataService(
            FieldSearchDao fieldSearchDao,
            WellSearchDao wellSearchDao,
            ProductionSearchDao productionSearchDao
    ) {
        this.fieldSearchDao = fieldSearchDao;
        this.wellSearchDao = wellSearchDao;
        this.productionSearchDao = productionSearchDao;
    }

    public void loadTestData() {
        System.out.println();
        System.out.println("=== BULK-ЗАГРУЗКА ТЕСТОВЫХ ДАННЫХ ===");

        fieldSearchDao.bulkIndex(createFields());
        wellSearchDao.bulkIndex(createWells());
        productionSearchDao.bulkIndex(createProductions());

        System.out.println("Тестовые данные загружены через bulk-запрос");
    }

    private List<FieldDocument> createFields() {
        return List.of(
                new FieldDocument(
                        "bulk-field-1",
                        "Самотлорское",
                        "Ханты-Мансийский АО",
                        61.154,
                        76.684,
                        "1965-05-29",
                        "Крупное нефтяное месторождение в Западной Сибири"
                ),
                new FieldDocument(
                        "bulk-field-2",
                        "Приобское",
                        "Ханты-Мансийский АО",
                        61.0,
                        73.5,
                        "1982-11-15",
                        "Нефтяное месторождение с высокой долей добывающих скважин"
                ),
                new FieldDocument(
                        "bulk-field-3",
                        "Ромашкинское",
                        "Республика Татарстан",
                        54.9,
                        52.3,
                        "1948-07-25",
                        "Одно из крупнейших нефтяных месторождений Волго-Уральской нефтегазоносной провинции"
                )
        );
    }

    private List<WellDocument> createWells() {
        return List.of(
                new WellDocument(
                        "bulk-well-1",
                        "bulk-field-1",
                        "Самотлорское",
                        "Ханты-Мансийский АО",
                        "СМ-101",
                        "PRODUCER",
                        "ACTIVE",
                        3050.0,
                        0.22,
                        "2020-05-12",
                        "Добывающая скважина с электроцентробежным насосом. Стабильная добыча нефти."
                ),
                new WellDocument(
                        "bulk-well-2",
                        "bulk-field-1",
                        "Самотлорское",
                        "Ханты-Мансийский АО",
                        "СМ-102",
                        "PRODUCER",
                        "ACTIVE",
                        3400.0,
                        0.25,
                        "2021-03-18",
                        "Глубокая добывающая скважина с повышенным дебитом нефти."
                ),
                new WellDocument(
                        "bulk-well-3",
                        "bulk-field-1",
                        "Самотлорское",
                        "Ханты-Мансийский АО",
                        "СМ-201",
                        "INJECTOR",
                        "ACTIVE",
                        2900.0,
                        0.20,
                        "2019-09-10",
                        "Нагнетательная скважина для поддержания пластового давления."
                ),
                new WellDocument(
                        "bulk-well-4",
                        "bulk-field-2",
                        "Приобское",
                        "Ханты-Мансийский АО",
                        "ПР-101",
                        "PRODUCER",
                        "MAINTENANCE",
                        3600.0,
                        0.25,
                        "2022-01-21",
                        "Скважина временно переведена в обслуживание после снижения добычи."
                ),
                new WellDocument(
                        "bulk-well-5",
                        "bulk-field-2",
                        "Приобское",
                        "Ханты-Мансийский АО",
                        "ПР-301",
                        "EXPLORATION",
                        "PLANNED",
                        1800.0,
                        0.16,
                        "2026-01-01",
                        "Разведочная скважина для уточнения геологической модели пласта."
                ),
                new WellDocument(
                        "bulk-well-6",
                        "bulk-field-3",
                        "Ромашкинское",
                        "Республика Татарстан",
                        "РМ-101",
                        "PRODUCER",
                        "ACTIVE",
                        2800.0,
                        0.18,
                        "2018-06-30",
                        "Добывающая скважина со стабильным режимом работы и умеренной обводненностью."
                )
        );
    }

    private List<ProductionDocument> createProductions() {
        return List.of(
                new ProductionDocument(
                        "bulk-production-1",
                        "bulk-field-1",
                        "Самотлорское",
                        "bulk-well-1",
                        "СМ-101",
                        "2026-03-10",
                        120.5,
                        540.0,
                        18.2,
                        "Стабильный режим добычи без превышения обводненности"
                ),
                new ProductionDocument(
                        "bulk-production-2",
                        "bulk-field-1",
                        "Самотлорское",
                        "bulk-well-2",
                        "СМ-102",
                        "2026-03-10",
                        160.0,
                        620.0,
                        21.5,
                        "Повышенная добыча нефти на глубокой скважине"
                ),
                new ProductionDocument(
                        "bulk-production-3",
                        "bulk-field-1",
                        "Самотлорское",
                        "bulk-well-3",
                        "СМ-201",
                        "2026-03-10",
                        0.0,
                        0.0,
                        600.0,
                        "Нагнетательная скважина, добыча нефти отсутствует"
                ),
                new ProductionDocument(
                        "bulk-production-4",
                        "bulk-field-2",
                        "Приобское",
                        "bulk-well-4",
                        "ПР-101",
                        "2026-03-10",
                        45.0,
                        300.0,
                        80.0,
                        "Снижение добычи, требуется анализ режима работы"
                ),
                new ProductionDocument(
                        "bulk-production-5",
                        "bulk-field-3",
                        "Ромашкинское",
                        "bulk-well-6",
                        "РМ-101",
                        "2026-03-10",
                        95.0,
                        410.0,
                        35.0,
                        "Умеренная добыча со стабильными показателями"
                )
        );
    }
}