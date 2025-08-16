package com.yourorg.kmapi.controller;
import com.yourorg.kmapi.dto.article.ArticleResponse;
import com.yourorg.kmapi.dto.search.*; import com.yourorg.kmapi.service.SearchService;
import jakarta.validation.Valid; import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api") @RequiredArgsConstructor
public class SearchController {
  private final SearchService searchService;
  @PostMapping("/search") public ResponseEntity<SearchResult<ArticleResponse>> search(@Valid @RequestBody SearchRequest req) { return ResponseEntity.ok(searchService.search(req)); }
}
