package org.godigit.ikm.service;

import lombok.RequiredArgsConstructor;
import org.godigit.ikm.dto.article.ArticleResponse;
import org.godigit.ikm.dto.search.SearchRequest;
import org.godigit.ikm.dto.search.SearchResult;
import org.godigit.ikm.entities.Article;
import org.godigit.ikm.mapper.SearchMapper;
import org.godigit.ikm.repository.ArticleRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

  private final ArticleRepository articleRepository;
  private final SearchMapper searchMapper;

  @Transactional(readOnly = true)
  public SearchResult<ArticleResponse> search(SearchRequest req) {
    int page = (req.page() == null || req.page() < 0) ? 0 : req.page();
    int size = (req.size() == null || req.size() <= 0) ? 10 : Math.min(req.size(), 100);

    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));

    Page<Article> pageResult = articleRepository.search(
            req.keyword(),
            req.departmentCode(),
            req.tags(),
            req.title(),
            req.id(),
            pageable
    );

    List<ArticleResponse> items = pageResult.getContent()
            .stream()
            .map(searchMapper::toResponse)
            .collect(Collectors.toList());

    return new SearchResult<>(
            items,
            pageResult.getTotalElements(),
            page,
            size
    );
  }

  @Transactional(readOnly = true)
  public ArticleResponse searchById(Long id) {
    Article article = articleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Article not found with ID: " + id));
    return searchMapper.toResponse(article);
  }


  @Transactional(readOnly = true)
  public List<ArticleResponse> searchByTitle(String title) {
    List<Article> articles = articleRepository.findByTitleContainingIgnoreCase(title);
    return articles.stream()
            .map(searchMapper::toResponse)
            .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<ArticleResponse> searchByTags(Set<String> tagNames) {
    List<Article> articles = articleRepository.findByTagNames(tagNames);
    return articles.stream()
            .map(searchMapper::toResponse)
            .collect(Collectors.toList());
  }


  @Transactional(readOnly = true)
  public List<ArticleResponse> searchByDepartment(String departmentCode) {
    List<Article> articles = articleRepository.findByDepartmentCode(departmentCode);
    return articles.stream()
            .map(searchMapper::toResponse)
            .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<ArticleResponse> searchByKeyword(String keyword) {
    List<Article> articles = articleRepository.findByBodyContainingIgnoreCase(keyword); // ✅ updated
    return articles.stream()
            .map(searchMapper::toResponse)
            .collect(Collectors.toList());
  }
}
