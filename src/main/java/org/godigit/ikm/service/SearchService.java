package org.godigit.ikm.service;

import lombok.RequiredArgsConstructor;
import org.godigit.ikm.dto.article.ArticleResponse;
import org.godigit.ikm.dto.search.SearchRequest;
import org.godigit.ikm.dto.search.SearchResult;
import org.godigit.ikm.entity.Article;
import org.godigit.ikm.mapper.SearchMapper;
import org.godigit.ikm.repository.ArticleRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors; // needed if you're on Java 11

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

    // If you're on Java 16+, you can use .toList()
    List<ArticleResponse> items = pageResult.getContent()
            .stream()
            .map(searchMapper::toResponse)
            .collect(Collectors.toList()); // replace with .toList() on Java 16+

    return new SearchResult<>(
            items,
            pageResult.getTotalElements(),
            page,
            size
    );
  }
}
