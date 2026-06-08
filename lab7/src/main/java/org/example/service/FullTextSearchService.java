package org.example.service;

import org.example.dao.WellSearchDao;
import org.example.domain.document.WellDocument;
import org.example.dto.SearchResultDto;

import java.util.List;

public class FullTextSearchService {

    private final WellSearchDao wellSearchDao;

    public FullTextSearchService(WellSearchDao wellSearchDao) {
        this.wellSearchDao = wellSearchDao;
    }

    public void run() {
        System.out.println();
        System.out.println("=== ПОЛНОТЕКСТОВЫЙ ПОИСК ПО DESCRIPTION ===");
        System.out.println("Ищем: добывающая скважина");

        printResults(wellSearchDao.searchByDescription("добывающая скважина"));

        System.out.println();
        System.out.println("=== MULTI_MATCH ПО НЕСКОЛЬКИМ ПОЛЯМ ===");
        System.out.println("Ищем по number, fieldName, description: Самотлорское нефть");

        printResults(wellSearchDao.multiMatchSearch("Самотлорское нефть"));

        System.out.println();
        System.out.println("=== FUZZY SEARCH С ОШИБКОЙ В СЛОВЕ ===");
        System.out.println("Ищем: добывающя");

        printResults(wellSearchDao.fuzzySearchByDescription("добывающя"));

        System.out.println();
        System.out.println("=== BOOL QUERY: ПОИСК + ФИЛЬТРЫ + СОРТИРОВКА + ПАГИНАЦИЯ ===");
        System.out.println("description содержит 'скважина', type=PRODUCER, status=ACTIVE, depth >= 2800");

        printResults(wellSearchDao.boolSearch(
                "скважина",
                "PRODUCER",
                "ACTIVE",
                2800.0,
                0,
                5
        ));
    }

    private void printResults(List<SearchResultDto<WellDocument>> results) {
        if (results.isEmpty()) {
            System.out.println("Документы не найдены");
            return;
        }

        for (SearchResultDto<WellDocument> result : results) {
            System.out.println();
            System.out.println("id: " + result.id());
            System.out.println("document: " + result.document());

            if (!result.highlights().isEmpty()) {
                System.out.println("highlight:");
                result.highlights().forEach((field, fragments) -> {
                    System.out.println("  " + field + ":");
                    fragments.forEach(fragment -> System.out.println("    " + fragment));
                });
            }
        }
    }
}