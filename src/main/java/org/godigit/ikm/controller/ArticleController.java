//package org.godigit.ikm.controller;
//
//import org.godigit.ikm.dto.article.*;
//import org.godigit.ikm.service.ArticleService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/articles")
//@RequiredArgsConstructor
//public class ArticleController {
//    private final ArticleService articleService;
//
//    @PostMapping
//    public ResponseEntity<ArticleResponse> create(@Valid @RequestBody ArticleCreateRequest req) {
//        return ResponseEntity.ok(articleService.create(req));
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ArticleResponse> get(@PathVariable("id") Long id) {
//        return ResponseEntity.ok(articleService.get(id));
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<ArticleResponse> update(@PathVariable("id") Long id, @Valid @RequestBody ArticleUpdateRequest req) {
//        return ResponseEntity.ok(articleService.update(id, req));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
//        articleService.delete(id);
//        return ResponseEntity.noContent().build();
//    }
//}
package org.godigit.ikm.controller;

import org.godigit.ikm.dto.article.*;
import org.godigit.ikm.entities.Role;
import org.godigit.ikm.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    // Create (any logged-in role could create; adjust if you want stricter)
    @PostMapping
    public ResponseEntity<ArticleResponse> create(@Valid @RequestBody ArticleCreateRequest req) {
        return ResponseEntity.ok(articleService.create(req));
    }

    // List
    @GetMapping
    public ResponseEntity<List<ArticleResponse>> list() {
        return ResponseEntity.ok(articleService.list());
    }

    // Get by id
    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> get(@PathVariable("id") Long id) {
        return ResponseEntity.ok(articleService.get(id));
    }

    // Update (ADMIN, CONTRIBUTOR)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody ArticleUpdateRequest req,
            @RequestHeader(value = "X-Role", defaultValue = "VIEWER") String roleHeader
    ) {
        Role role = parseRole(roleHeader);
        if (!role.canUpdate()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You do not have permission to update articles");
        }
        return ResponseEntity.ok(articleService.update(id, req));
    }

    // Delete (ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable("id") Long id,
            @RequestHeader(value = "X-Role", defaultValue = "VIEWER") String roleHeader
    ) {
        Role role = parseRole(roleHeader);
        if (!role.canDelete()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You do not have permission to delete articles");
        }
        articleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ---- helpers ----
    private Role parseRole(String header) {
        try {
            return Role.valueOf(header.trim().toUpperCase());
        } catch (Exception ex) {
            return Role.VIEWER; // fail-safe
        }
    }
}
