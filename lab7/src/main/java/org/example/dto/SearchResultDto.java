package org.example.dto;

import java.util.List;
import java.util.Map;

public record SearchResultDto<T>(
        String id,
        T document,
        Map<String, List<String>> highlights
) {
}