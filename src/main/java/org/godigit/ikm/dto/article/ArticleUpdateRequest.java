package org.godigit.ikm.dto.article;
import jakarta.validation.constraints.*; import java.util.Set;
public record ArticleUpdateRequest(@NotBlank String title, @NotBlank String body, Set<String> tags) {}
