package com.morning.morningpage.controller;

import com.morning.morningpage.dto.CategoryResponse;
import com.morning.morningpage.entity.Category;
import com.morning.morningpage.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    
    private final CategoryService categoryService;
    
    // 전체 카테고리 조회
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getAllCategories()
                .stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }
    
    // 특정 카테고리 조회
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(CategoryResponse.from(category));
    }
}
