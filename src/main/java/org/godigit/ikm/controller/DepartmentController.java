package org.godigit.ikm.controller;

import lombok.RequiredArgsConstructor;
import org.godigit.ikm.entities.Department;
import org.godigit.ikm.dto.article.ArticleResponse;
import org.godigit.ikm.mapper.ArticleMapper;
import org.godigit.ikm.repository.ArticleRepository;
import org.godigit.ikm.repository.DepartmentRepository;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentRepository departmentRepository;
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

    // List departments with pagination
    @GetMapping
    public ResponseEntity<Page<Department>> list(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 100));
        Page<Department> departments = departmentRepository.findAll(pageable);
        return ResponseEntity.ok(departments);
    }

    // Get department by ID
    @GetMapping("/{id}")
    public ResponseEntity<Department> get(@PathVariable("id") Long id) {
        return ResponseEntity.of(departmentRepository.findById(id));
    }

    // Get department by code
    @GetMapping("/by-code/{code}")
    public ResponseEntity<Department> byCode(@PathVariable("code") String code) {
        return ResponseEntity.of(departmentRepository.findByCode(code));
    }

    // Get paginated articles within a department
    @GetMapping("/{id}/articles")
    public ResponseEntity<Page<ArticleResponse>> articles(
            @PathVariable("id") Long id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        if (departmentRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 100));
        Page<ArticleResponse> pageArticles = articleRepository.findByDepartmentId(id, pageable)
                .map(articleMapper::toResponse);

        return ResponseEntity.ok(pageArticles);
    }
}
