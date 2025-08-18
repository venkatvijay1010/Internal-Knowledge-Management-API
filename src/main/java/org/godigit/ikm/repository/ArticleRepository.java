package org.godigit.ikm.repository;
import org.godigit.ikm.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ArticleRepository extends JpaRepository<Article, Long> {}
