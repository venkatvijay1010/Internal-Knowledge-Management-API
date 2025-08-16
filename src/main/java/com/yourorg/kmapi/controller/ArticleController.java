package com.yourorg.kmapi.controller;
import com.yourorg.kmapi.dto.article.*; import com.yourorg.kmapi.service.ArticleService;
import jakarta.validation.Valid; import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/articles") @RequiredArgsConstructor
public class ArticleController {
  private final ArticleService articleService;
  @PostMapping public ResponseEntity<ArticleResponse> create(@Valid @RequestBody ArticleCreateRequest req) { return ResponseEntity.ok(articleService.create(req)); }
  @GetMapping("/{id}") public ResponseEntity<ArticleResponse> get(@PathVariable Long id) { return ResponseEntity.ok(articleService.get(id)); }
  @PutMapping("/{id}") public ResponseEntity<ArticleResponse> update(@PathVariable Long id, @Valid @RequestBody ArticleUpdateRequest req) { return ResponseEntity.ok(articleService.update(id, req)); }
  @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) { articleService.delete(id); return ResponseEntity.noContent().build(); }
}
