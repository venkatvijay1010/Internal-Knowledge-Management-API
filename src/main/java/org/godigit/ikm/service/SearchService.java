package org.godigit.ikm.service;
import org.godigit.ikm.dto.article.ArticleResponse;
import org.godigit.ikm.dto.search.*; import org.godigit.ikm.repository.search.ArticleSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
@Service @RequiredArgsConstructor
public class SearchService {
  private final ArticleSearchRepository searchRepository;
  public SearchResult<ArticleResponse> search(SearchRequest req) {
    int page = req.page() == null ? 0 : Math.max(req.page(), 0);
    int size = req.size() == null ? 20 : Math.min(Math.max(req.size(), 1), 100);
    var pg = PageRequest.of(page, size);
    var res = searchRepository.search(req.keyword(), req.departmentCode(), req.tags(), pg);
    var items = res.getContent().stream().map(a -> new ArticleResponse(
      a.getId(), a.getTitle(), a.getBody(), a.getDepartment().getCode(),
      a.getCurrentVersion(), a.getCreatedAt(), a.getUpdatedAt(),
      a.getTags().stream().map(t -> t.getName()).collect(Collectors.toSet())
    )).toList();
    return new SearchResult<>(items, res.getTotalElements(), page, size);
  }
}
