package org.godigit.ikm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.godigit.ikm.dto.article.ArticleResponse;
import org.godigit.ikm.dto.search.SearchRequest;
import org.godigit.ikm.dto.search.SearchResult;
import org.godigit.ikm.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    // Flexible search with multiple filters (POST)
    @PostMapping("/search")
    public ResponseEntity<SearchResult<ArticleResponse>> search(@Valid @RequestBody SearchRequest req) {
        return ResponseEntity.ok(searchService.search(req));
    }

    // Flexible search with query params (GET)
    @GetMapping("/search")
    public ResponseEntity<SearchResult<ArticleResponse>> searchGet(
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "departmentCode", required = false) String departmentCode,
            @RequestParam(name = "tags", required = false) List<String> tags,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Set<String> tagSet = (tags == null || tags.isEmpty()) ? null : new LinkedHashSet<>(splitCsvIfNeeded(tags));

        SearchRequest req = new SearchRequest(id, keyword, title, departmentCode, tagSet, page, size);
        return ResponseEntity.ok(searchService.search(req));
    }

    // Search by ID
    @GetMapping("/search/by-id")
    public ResponseEntity<ArticleResponse> searchById(@RequestParam(name = "id") Long id) {
        return ResponseEntity.ok(searchService.searchById(id));
    }

    // Search by Title
    @GetMapping("/search/by-title")
    public ResponseEntity<List<ArticleResponse>> searchByTitle(@RequestParam(name = "title") String title) {
        return ResponseEntity.ok(searchService.searchByTitle(title));
    }

    // Search by Tags
    @GetMapping("/search/by-tags")
    public ResponseEntity<List<ArticleResponse>> searchByTags(@RequestParam(name = "tags") List<String> tags) {
        Set<String> tagSet = new HashSet<>(splitCsvIfNeeded(tags));
        return ResponseEntity.ok(searchService.searchByTags(tagSet));
    }

    // Search by Department Code
    @GetMapping("/search/by-department")
    public ResponseEntity<List<ArticleResponse>> searchByDepartment(@RequestParam(name = "departmentCode") String departmentCode) {
        return ResponseEntity.ok(searchService.searchByDepartment(departmentCode));
    }

    // Search by Keyword
    @GetMapping("/search/by-keyword")
    public ResponseEntity<List<ArticleResponse>> searchByKeyword(@RequestParam(name = "keyword") String keyword) {
        return ResponseEntity.ok(searchService.searchByKeyword(keyword));
    }

    // Helper method to support CSV and repeated query params
    private List<String> splitCsvIfNeeded(List<String> raw) {
        return raw.stream()
                .filter(Objects::nonNull)
                .flatMap(s -> Stream.of(s.split(",")))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}
