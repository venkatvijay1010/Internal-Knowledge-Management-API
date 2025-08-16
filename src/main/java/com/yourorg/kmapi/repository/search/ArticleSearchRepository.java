package com.yourorg.kmapi.repository.search;
import com.yourorg.kmapi.entity.Article;
import org.springframework.data.domain.Page; import org.springframework.data.domain.Pageable;
import java.util.Set;
public interface ArticleSearchRepository {
  Page<Article> search(String keyword, String deptCode, Set<String> tags, Pageable pageable);
}
