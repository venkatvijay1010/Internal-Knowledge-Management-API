package org.godigit.ikm.dto.search;

import java.util.Set;

public record SearchRequest(
        Long id,
        String keyword,
        String title,
        String departmentCode,
        Set<String> tags,
        Integer page,
        Integer size
) {}
