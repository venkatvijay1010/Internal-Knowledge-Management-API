/*
package org.godigit.ikm.controller;

import org.godigit.ikm.dto.article.ArticleResponse;
import org.godigit.ikm.dto.search.SearchRequest;
import org.godigit.ikm.dto.search.SearchResult;
import org.godigit.ikm.service.SearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    // Keep POST for programmatic/JSON clients
    @PostMapping("/search")
    public ResponseEntity<SearchResult<ArticleResponse>> search(@Valid @RequestBody SearchRequest req) {
        return ResponseEntity.ok(searchService.search(req));
    }

    // Add GET for browser/testing via URL
    @GetMapping("/search")
    public ResponseEntity<SearchResult<ArticleResponse>> searchGet(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String departmentCode,
            // Accept repeated params (?tags=spring&tags=boot) or CSV (?tags=spring,boot)
            @RequestParam(required = false) List<String> tags,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // Convert List<String> to Set<String>, preserve null if absent so service can treat it as "no filter"
        Set<String> tagSet = (tags == null || tags.isEmpty()) ? null : new HashSet<>(splitCsvIfNeeded(tags));

        SearchRequest req = new SearchRequest(
                id,
                keyword,
                title,
                departmentCode,
                tagSet,
                page,
                size
        );
        return ResponseEntity.ok(searchService.search(req));
    }

    */
/**
     * Supports both repeated query params and CSV values, e.g.:
     *   ?tags=spring&tags=boot
     *   ?tags=spring,boot
     *//*

    private List<String> splitCsvIfNeeded(List<String> raw) {
        return raw.stream()
                .flatMap(s -> List.of(s.split(",")).stream())
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
*/


package org.godigit.ikm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    // POST endpoint for JSON clients (Swagger, Insomnia, etc.)
    @PostMapping("/search")
    public ResponseEntity<SearchResult> search(@Valid @RequestBody SearchRequest req) {
        return ResponseEntity.ok(searchService.search(req));
    }

    // GET endpoint for browser-based testing
    @GetMapping("/search")
    public ResponseEntity<SearchResult> searchGet(
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "departmentCode", required = false) String departmentCode,
            @RequestParam(name = "tags", required = false) List<String> tags, // supports repeated params
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        // Convert tags to Set, handle CSV or repeated query params
        Set<String> tagSet = (tags == null || tags.isEmpty()) ? null : new LinkedHashSet<>(splitCsvIfNeeded(tags));

        SearchRequest req = new SearchRequest(
                id,
                keyword,
                title,
                departmentCode,
                tagSet,
                page,
                size
        );

        return ResponseEntity.ok(searchService.search(req));
    }

    /**
     * Supports both repeated query params and CSV values, e.g.:
     *   ?tags=spring&tags=boot
     *   ?tags=spring,boot
     */
    private List<String> splitCsvIfNeeded(List<String> raw) {
        return raw.stream()
                .filter(Objects::nonNull)
                .flatMap(s -> Stream.of(s.split(",")))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList()); // use toList() if you're on Java 16+
    }
}
