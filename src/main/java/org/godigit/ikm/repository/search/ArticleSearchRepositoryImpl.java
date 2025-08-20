package org.godigit.ikm.repository.search;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.godigit.ikm.entity.Article;
import org.godigit.ikm.entity.Department;
import org.godigit.ikm.entity.Tag;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Repository
public class ArticleSearchRepositoryImpl implements ArticleSearchRepository {

  @PersistenceContext
  private EntityManager em;

  @Override
  public Page<Article> search(String keyword,
                              String departmentCode,
                              Set<String> tags,
                              String title,
                              Long id,
                              Pageable pageable) {

    CriteriaBuilder cb = em.getCriteriaBuilder();

    // === main query ===
    CriteriaQuery<Article> cq = cb.createQuery(Article.class);
    Root<Article> article = cq.from(Article.class);
    Join<Article, Department> dept = article.join("department", JoinType.LEFT);

    boolean hasTags = tags != null && !tags.isEmpty();
    Join<Article, Tag> tagJoin = null;
    if (hasTags) {
      tagJoin = article.joinSet("tags", JoinType.LEFT);
    }

    List<Predicate> preds = new ArrayList<>();
    if (id != null) {
      preds.add(cb.equal(article.get("id"), id));
    }
    if (title != null && !title.isBlank()) {
      String like = "%" + title.toLowerCase() + "%";
      preds.add(cb.like(cb.lower(article.get("title")), like));
    }
    if (keyword != null && !keyword.isBlank()) {
      String like = "%" + keyword.toLowerCase() + "%";
      preds.add(cb.or(
              cb.like(cb.lower(article.get("title")), like),
              cb.like(cb.lower(article.get("body")), like)
      ));
    }
    if (departmentCode != null && !departmentCode.isBlank()) {
      preds.add(cb.equal(dept.get("code"), departmentCode));
    }
    if (hasTags) {
      preds.add(Objects.requireNonNull(tagJoin).get("name").in(tags)); // ANY-of tags
    }

    cq.select(article).where(preds.toArray(new Predicate[0]));
    cq.distinct(true); // avoid duplicates with tag joins

    // sorting
    if (pageable.getSort().isUnsorted()) {
      cq.orderBy(cb.desc(article.get("updatedAt")));
    } else {
      List<Order> orders = new ArrayList<>();
      for (Sort.Order o : pageable.getSort()) {
        Path<?> path = resolvePath(article, o.getProperty());
        orders.add(o.isAscending() ? cb.asc(path) : cb.desc(path));
      }
      cq.orderBy(orders);
    }

    TypedQuery<Article> query = em.createQuery(cq);
    query.setFirstResult((int) pageable.getOffset());
    query.setMaxResults(pageable.getPageSize());
    List<Article> content = query.getResultList();

    // === count query ===
    CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
    Root<Article> countRoot = countCq.from(Article.class);
    Join<Article, Department> countDept = countRoot.join("department", JoinType.LEFT);
    Join<Article, Tag> countTagJoin = null;
    if (hasTags) {
      countTagJoin = countRoot.joinSet("tags", JoinType.LEFT);
    }

    List<Predicate> countPreds = new ArrayList<>();
    if (id != null) countPreds.add(cb.equal(countRoot.get("id"), id));
    if (title != null && !title.isBlank()) {
      String like = "%" + title.toLowerCase() + "%";
      countPreds.add(cb.like(cb.lower(countRoot.get("title")), like));
    }
    if (keyword != null && !keyword.isBlank()) {
      String like = "%" + keyword.toLowerCase() + "%";
      countPreds.add(cb.or(
              cb.like(cb.lower(countRoot.get("title")), like),
              cb.like(cb.lower(countRoot.get("body")), like)
      ));
    }
    if (departmentCode != null && !departmentCode.isBlank()) {
      countPreds.add(cb.equal(countDept.get("code"), departmentCode));
    }
    if (hasTags) {
      countPreds.add(Objects.requireNonNull(countTagJoin).get("name").in(tags));
    }

    countCq.select(cb.countDistinct(countRoot)).where(countPreds.toArray(new Predicate[0]));
    long total = em.createQuery(countCq).getSingleResult();

    return new PageImpl<>(content, pageable, total);
  }

  @SuppressWarnings("unchecked")
  private Path<?> resolvePath(Root<Article> root, String property) {
    if (!property.contains(".")) return root.get(property);
    Path<?> path = root;
    for (String p : property.split("\\.")) {
      path = ((From<?, ?>) path).get(p);
    }
    return path;
  }
}
