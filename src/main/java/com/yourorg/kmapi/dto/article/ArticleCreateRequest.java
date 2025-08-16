package com.yourorg.kmapi.dto.article;
import jakarta.validation.constraints.*; import java.util.Set;
public record ArticleCreateRequest(
  @NotBlank String title,
  @NotBlank String body,
  @NotNull Long departmentId,
  Set<String> tags
) {}
