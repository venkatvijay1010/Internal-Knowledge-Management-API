package org.godigit.ikm.repository.search;

import org.godigit.ikm.entities.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface ArticleSearchRepository {
  Page<Article> search(String keyword, String departmentCode, Set<String> tags, String title, Long id, Pageable pageable);
}
