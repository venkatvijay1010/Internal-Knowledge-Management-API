package org.godigit.ikm.dto.search;

import java.util.List;

public record SearchResult<T>(
        List<T> items,
        long total,
        int page,
        int size
) {}
