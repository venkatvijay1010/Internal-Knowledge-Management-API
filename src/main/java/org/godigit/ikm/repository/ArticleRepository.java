package org.godigit.ikm.repository;

import org.godigit.ikm.entities.Article;
import org.godigit.ikm.repository.search.ArticleSearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleSearchRepository {

    // Search by title (case-insensitive, partial match)
    List<Article> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT DISTINCT a FROM Article a JOIN a.tags t WHERE t.name IN :tagNames")
    List<Article> findByTagNames(@Param("tagNames") Set<String> tagNames);


    // Search by department code
    List<Article> findByDepartmentCode(String departmentCode);

    // Search by keyword in body
    List<Article> findByBodyContainingIgnoreCase(String keyword);


}
