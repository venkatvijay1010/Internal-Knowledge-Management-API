package org.godigit.ikm.dto.search;
import java.util.Set;
public record SearchRequest(String keyword, Set<String> tags, String departmentCode, Integer page, Integer size) {}
