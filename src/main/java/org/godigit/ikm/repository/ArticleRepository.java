package org.godigit.ikm.repository;

import org.godigit.ikm.entity.Article;
import org.godigit.ikm.repository.search.ArticleSearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleSearchRepository {}
