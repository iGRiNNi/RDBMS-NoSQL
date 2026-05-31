package org.example.demo;

import org.example.dao.FieldWithWellDao;
import org.example.dto.FieldWithWellDto;

import java.util.List;

public class JoinDemo {

    private final FieldWithWellDao fieldWithWellDao = new FieldWithWellDao();

    public void run() {
        printJoinResult(
                "INNER JOIN",
                fieldWithWellDao.innerJoin()
        );

        printJoinResult(
                "LEFT JOIN",
                fieldWithWellDao.leftJoin()
        );

        printJoinResult(
                "RIGHT JOIN",
                fieldWithWellDao.rightJoin()
        );

        printJoinResult(
                "FULL JOIN",
                fieldWithWellDao.fullJoin()
        );

        printJoinResult(
                "CROSS JOIN",
                fieldWithWellDao.crossJoin()
        );
    }

    private void printJoinResult(String title, List<FieldWithWellDto> rows) {
        System.out.println();
        System.out.println("=== " + title + " ===");

        for (FieldWithWellDto row : rows) {
            System.out.printf(
                    "fieldId=%s, fieldName=%s, wellId=%s, wellNumber=%s, status=%s%n",
                    row.getFieldId(),
                    row.getFieldName(),
                    row.getWellId(),
                    row.getWellNumber(),
                    row.getWellStatus()
            );
        }
    }
}