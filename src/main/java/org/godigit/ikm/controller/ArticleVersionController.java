package org.godigit.ikm.controller;

import lombok.RequiredArgsConstructor;
import org.godigit.ikm.dto.article.ArticleResponse;
import org.godigit.ikm.entities.Article;
import org.godigit.ikm.entities.ArticleVersion;
import org.godigit.ikm.mapper.ArticleMapper;
import org.godigit.ikm.repository.ArticleRepository;
import org.godigit.ikm.repository.ArticleVersionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/articles/{articleId}/versions")
@RequiredArgsConstructor
public class ArticleVersionController {

    private final ArticleRepository articleRepository;
    private final ArticleVersionRepository versionRepository;
    private final ArticleMapper articleMapper;

    @GetMapping
    public ResponseEntity<List<ArticleVersion>> list(@PathVariable Long articleId) {
        if (articleRepository.findById(articleId).isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(versionRepository.findByArticle_Id(articleId));
    }

    @GetMapping("/{version}")
    public ResponseEntity<ArticleVersion> get(@PathVariable Long articleId,
                                              @PathVariable Integer version) {
        return ResponseEntity.of(versionRepository.findByArticle_IdAndVersion(articleId, version));
    }

    // Snapshot current article as a new version and bump currentVersion
    @PostMapping
    public ResponseEntity<ArticleResponse> snapshot(@PathVariable Long articleId) {
        Article article = articleRepository.findById(articleId).orElse(null);
        if (article == null) return ResponseEntity.notFound().build();

        int newVersion = (article.getCurrentVersion() == null ? 0 : article.getCurrentVersion()) + 1;
        var now = OffsetDateTime.now();

        ArticleVersion v = ArticleVersion.builder()
                .article(article)
                .version(newVersion)
                .title(article.getTitle())
                .body(article.getBody())
                .createdAt(now)
                .build();

        versionRepository.save(v);
        article.setCurrentVersion(newVersion);
        article.setUpdatedAt(now);
        articleRepository.save(article);

        return ResponseEntity.ok(articleMapper.toResponse(article));
    }
}
