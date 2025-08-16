package com.yourorg.kmapi.repository;
import com.yourorg.kmapi.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ArticleRepository extends JpaRepository<Article, Long> {}
