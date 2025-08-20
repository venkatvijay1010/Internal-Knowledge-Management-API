package org.godigit.ikm.service;

import org.godigit.ikm.dto.article.*;
import org.godigit.ikm.entity.*;
import org.godigit.ikm.mapper.ArticleMapper;
import org.godigit.ikm.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {

  private final ArticleRepository articleRepository;
  private final ArticleVersionRepository articleVersionRepository;
  private final DepartmentRepository departmentRepository;
  private final TagService tagService;
  private final ArticleMapper articleMapper;

  @Transactional
  public ArticleResponse create(ArticleCreateRequest req) {
    Department dept = departmentRepository.findById(req.departmentId())
            .orElseThrow(() -> new IllegalArgumentException("Department not found"));

    OffsetDateTime now = OffsetDateTime.now();

    Article article = Article.builder()
            .title(req.title())
            .body(req.body())
            .department(dept)
            .currentVersion(1)
            .createdAt(now)
            .updatedAt(now)
            .build();

    article.setTags(tagService.ensureTags(req.tags()));

    // Ensure ID and timestamps are populated
    article = articleRepository.saveAndFlush(article);

    ArticleVersion version = ArticleVersion.builder()
            .article(article)
            .version(1)
            .title(article.getTitle())
            .body(article.getBody())
            .build();

    articleVersionRepository.save(version);

    return articleMapper.toResponse(article);
  }

  @Transactional
  public ArticleResponse update(Long id, ArticleUpdateRequest req) {
    Article article = articleRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Article not found"));

    article.setTitle(req.title());
    article.setBody(req.body());
    article.setTags(tagService.ensureTags(req.tags()));
    article.setCurrentVersion(article.getCurrentVersion() + 1);
    article.setUpdatedAt(OffsetDateTime.now());

    article = articleRepository.saveAndFlush(article);

    ArticleVersion version = ArticleVersion.builder()
            .article(article)
            .version(article.getCurrentVersion())
            .title(article.getTitle())
            .body(article.getBody())
            .build();

    articleVersionRepository.save(version);

    return articleMapper.toResponse(article);
  }

  @Transactional(readOnly = true)
  public ArticleResponse get(Long id) {
    return articleRepository.findById(id)
            .map(articleMapper::toResponse)
            .orElseThrow(() -> new IllegalArgumentException("Article not found"));
  }

  @Transactional(readOnly = true)
  public List<ArticleResponse> list() {
    return articleRepository.findAll().stream()
            .map(articleMapper::toResponse)
            .collect(Collectors.toList());
  }

  @Transactional
  public void delete(Long id) {
    articleRepository.deleteById(id);
  }
}
