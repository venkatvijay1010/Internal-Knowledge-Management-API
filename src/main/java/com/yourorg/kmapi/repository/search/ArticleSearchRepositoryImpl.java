package com.yourorg.kmapi.repository.search;

import com.yourorg.kmapi.entity.Article;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ArticleSearchRepositoryImpl implements ArticleSearchRepository {

  @PersistenceContext
  private EntityManager em;

  @Override
  public Page<Article> search(String keyword, String deptCode, Set<String> tags, Pageable pageable) {
    StringBuilder sb = new StringBuilder();
    List<Object> params = new ArrayList<>();
    sb.append("SELECT a.* FROM articles a ");
    sb.append("JOIN departments d ON a.department_id = d.id ");
    sb.append("LEFT JOIN article_tags at ON at.article_id = a.id ");
    sb.append("LEFT JOIN tags t ON t.id = at.tag_id ");
    sb.append("WHERE 1=1 ");

    if (keyword != null && !keyword.isBlank()) {
      sb.append(" AND (unaccent(a.title) ILIKE unaccent(?) OR unaccent(a.body) ILIKE unaccent(?))");
      String like = "%" + keyword + "%";
      params.add(like);
      params.add(like);
    }
    if (deptCode != null && !deptCode.isBlank()) {
      sb.append(" AND d.code = ?");
      params.add(deptCode);
    }
    if (tags != null && !tags.isEmpty()) {
      sb.append(" AND t.name = ANY (?)");
      params.add(tags.toArray(new String[0]));
    }
    sb.append(" GROUP BY a.id ORDER BY a.updated_at DESC");

    var q = em.createNativeQuery(sb.toString(), Article.class);
    for (int i = 0; i < params.size(); i++) {
      q.setParameter(i + 1, params.get(i));
    }
    q.setFirstResult((int) pageable.getOffset());
    q.setMaxResults(pageable.getPageSize());
    @SuppressWarnings("unchecked")
    List<Article> content = q.getResultList();

    String countSql = "SELECT count(DISTINCT a.id) FROM (" + sb.toString().replace("SELECT a.*", "SELECT a.id") + ") q";
    var countQ = em.createNativeQuery(countSql);
    for (int i = 0; i < params.size(); i++) {
      countQ.setParameter(i + 1, params.get(i));
    }
    Number total = (Number) countQ.getSingleResult();
    return new PageImpl<>(content, pageable, total.longValue());
  }
}
