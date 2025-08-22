package org.godigit.ikm.repository;
import org.godigit.ikm.entities.ArticleVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleVersionRepository extends JpaRepository<ArticleVersion, Long> {
    List<ArticleVersion> findByArticle_Id(Long articleId);
    Optional<ArticleVersion> findByArticle_IdAndVersion(Long articleId, Integer version);
}
