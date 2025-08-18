package org.godigit.ikm.dto.article;
import java.time.OffsetDateTime; import java.util.Set;
public record ArticleResponse(
  Long id, String title, String body, String departmentCode, Integer currentVersion,
  OffsetDateTime createdAt, OffsetDateTime updatedAt, Set<String> tags
) {}
