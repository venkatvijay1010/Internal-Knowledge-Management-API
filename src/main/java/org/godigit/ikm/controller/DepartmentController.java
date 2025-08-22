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

    @GetMapping
    public ResponseEntity<Page<Department>> list(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "50") int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 100));
        var departments = departmentRepository.findAll(pageable);
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> get(@PathVariable Long id) {
        return ResponseEntity.of(departmentRepository.findById(id));
    }

    @GetMapping("/by-code/{code}")
    public ResponseEntity<Department> byCode(@PathVariable String code) {
        return ResponseEntity.of(departmentRepository.findByCode(code));
    }

    // Articles within a department (paginated)
    @GetMapping("/{id}/articles")
    public ResponseEntity<Page<ArticleResponse>> articles(@PathVariable Long id,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        if (departmentRepository.findById(id).isEmpty()) return ResponseEntity.notFound().build();
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 100));
        var pageArticles = articleRepository.findByDepartmentId(id, pageable)
                .map(articleMapper::toResponse);
        return ResponseEntity.ok(pageArticles);
    }
}
