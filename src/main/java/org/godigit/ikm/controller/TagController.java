package org.godigit.ikm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.godigit.ikm.entities.Tag;
import org.godigit.ikm.entities.Role;
import org.godigit.ikm.dto.article.ArticleResponse;
import org.godigit.ikm.mapper.ArticleMapper;
import org.godigit.ikm.repository.ArticleRepository;
import org.godigit.ikm.repository.TagRepository;
import org.godigit.ikm.service.TagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagRepository tagRepository;
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final TagService tagService;

    // Create tag (ADMIN/CONTRIBUTOR)
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Tag req,
                                    @RequestHeader(value = "X-Role", defaultValue = "VIEWER") String roleHeader) {
        Role role = parseRole(roleHeader);
        if (!role.canUpdate()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Insufficient permission to create tags");
        }
        String name = req.getName() == null ? "" : req.getName().trim();
        if (name.isEmpty()) return ResponseEntity.badRequest().body("Tag name required");
        Tag saved = tagRepository.findByNameIgnoreCase(name)
                .orElseGet(() -> tagRepository.save(Tag.builder().name(name).build()));
        return ResponseEntity.ok(saved);
    }

    // List tags
    @GetMapping
    public ResponseEntity<List<Tag>> list(@RequestParam(value = "q", required = false) String q) {
        List<Tag> all = tagRepository.findAll();
        if (q == null || q.trim().isEmpty()) return ResponseEntity.ok(all);
        String needle = q.trim().toLowerCase();
        return ResponseEntity.ok(
                all.stream().filter(t -> t.getName() != null && t.getName().toLowerCase().contains(needle))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> get(@PathVariable Long id) {
        return ResponseEntity.of(tagRepository.findById(id));
    }

    // Delete tag (ADMIN only)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @RequestHeader(value = "X-Role", defaultValue = "VIEWER") String roleHeader) {
        Role role = parseRole(roleHeader);
        if (!role.canDelete()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only ADMIN can delete tags");
        }
        tagRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Articles under a tag (paginated)
    @GetMapping("/{id}/articles")
    public ResponseEntity<Page<ArticleResponse>> articlesByTag(@PathVariable Long id,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        var tag = tagRepository.findById(id).orElse(null);
        if (tag == null) return ResponseEntity.notFound().build();
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 100));
        var articles = articleRepository.findByTagId(id, pageable)
                .map(articleMapper::toResponse);
        return ResponseEntity.ok(articles);
    }

    // Attach tags to an article by names (helper endpoint kept here for simplicity)
    @PostMapping("/attach-to-article/{articleId}")
    public ResponseEntity<?> attachToArticle(@PathVariable Long articleId,
                                             @RequestBody List<String> names,
                                             @RequestHeader(value = "X-Role", defaultValue = "VIEWER") String roleHeader) {
        Role role = parseRole(roleHeader);
        if (!role.canUpdate()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Insufficient permission to attach tags");
        }
        Set<String> n = normalize(names);
        if (n.isEmpty()) return ResponseEntity.badRequest().body("Provide at least one non-empty tag name");
        tagService.ensureTags(n); // ensures they exist
        // attachment is already implemented on ArticleController in your project; if you prefer it here,
        // we can move that logic — for now this endpoint just ensures tags exist.
        return ResponseEntity.ok().build();
    }

    private Role parseRole(String header) {
        try { return Role.valueOf(header.trim().toUpperCase()); } catch (Exception e) { return Role.VIEWER; }
    }
    private Set<String> normalize(List<String> raw) {
        if (raw == null) return Set.of();
        return raw.stream().filter(Objects::nonNull).map(String::trim)
                .map(String::toLowerCase).filter(s -> !s.isEmpty())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
