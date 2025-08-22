package org.godigit.ikm.repository;

import org.godigit.ikm.entities.Article;
import org.godigit.ikm.repository.search.ArticleSearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT a FROM Article a WHERE a.department.id = :deptId")
    Page<Article> findByDepartmentId(@Param("deptId") Long departmentId, Pageable pageable);

    @Query("SELECT DISTINCT a FROM Article a JOIN a.tags t WHERE t.id = :tagId")
    Page<Article> findByTagId(@Param("tagId") Long tagId, Pageable pageable);

    // Search by department code
    List<Article> findByDepartmentCode(String departmentCode);

    // Search by keyword in body
    List<Article> findByBodyContainingIgnoreCase(String keyword);


}
