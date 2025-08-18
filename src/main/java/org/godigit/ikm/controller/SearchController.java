package org.godigit.ikm.controller;
import org.godigit.ikm.dto.article.ArticleResponse;
import org.godigit.ikm.dto.search.*; import org.godigit.ikm.service.SearchService;
import jakarta.validation.Valid; import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api") @RequiredArgsConstructor
public class SearchController {
  private final SearchService searchService;
  @PostMapping("/search") public ResponseEntity<SearchResult<ArticleResponse>> search(@Valid @RequestBody SearchRequest req) { return ResponseEntity.ok(searchService.search(req)); }
}
