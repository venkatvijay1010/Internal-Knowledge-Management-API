package org.godigit.ikm.service;
import org.godigit.ikm.dto.article.*; import org.godigit.ikm.entity.*;
import org.godigit.ikm.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime; import java.util.Set; import java.util.stream.Collectors;
@Service @RequiredArgsConstructor
public class ArticleService {
  private final ArticleRepository articleRepository;
  private final ArticleVersionRepository articleVersionRepository;
  private final DepartmentRepository departmentRepository;
  private final TagService tagService;

  @Transactional
  public ArticleResponse create(ArticleCreateRequest req) {
    Department dept = departmentRepository.findById(req.departmentId()).orElseThrow(() -> new IllegalArgumentException("Department not found"));
    var now = OffsetDateTime.now();
    Article article = Article.builder().title(req.title()).body(req.body()).department(dept).currentVersion(1).createdAt(now).updatedAt(now).build();
    article.setTags(tagService.ensureTags(req.tags()));
    article = articleRepository.save(article);
    ArticleVersion v = ArticleVersion.builder().article(article).version(1).title(article.getTitle()).body(article.getBody()).build();
    articleVersionRepository.save(v);
    return map(article);
  }

  @Transactional
  public ArticleResponse update(Long id, ArticleUpdateRequest req) {
    Article article = articleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Article not found"));
    article.setTitle(req.title()); article.setBody(req.body()); article.setTags(tagService.ensureTags(req.tags()));
    article.setCurrentVersion(article.getCurrentVersion()+1); article.setUpdatedAt(OffsetDateTime.now());
    article = articleRepository.save(article);
    ArticleVersion v = ArticleVersion.builder().article(article).version(article.getCurrentVersion()).title(article.getTitle()).body(article.getBody()).build();
    articleVersionRepository.save(v);
    return map(article);
  }

  @Transactional(readOnly = true)
  public ArticleResponse get(Long id) {
    return articleRepository.findById(id).map(this::map).orElseThrow(() -> new IllegalArgumentException("Article not found"));
  }

  @Transactional public void delete(Long id) { articleRepository.deleteById(id); }

  private ArticleResponse map(Article a) {
    Set<String> tags = a.getTags().stream().map(Tag::getName).collect(Collectors.toSet());
    return new ArticleResponse(a.getId(), a.getTitle(), a.getBody(), a.getDepartment().getCode(), a.getCurrentVersion(), a.getCreatedAt(), a.getUpdatedAt(), tags);
  }
}
