package com.morning.morningpage.service;

import com.morning.morningpage.entity.Category;
import com.morning.morningpage.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    // 애플리케이션 시작 시 기본 카테고리 생성
    @PostConstruct
    @Transactional
    public void initCategories() {
        if (categoryRepository.count() == 0) {
            categoryRepository.save(Category.builder()
                    .name("운동")
                    .icon("💪")
                    .color("#FF6B6B")
                    .build());
            
            categoryRepository.save(Category.builder()
                    .name("독서")
                    .icon("📚")
                    .color("#4ECDC4")
                    .build());
            
            categoryRepository.save(Category.builder()
                    .name("명상")
                    .icon("🧘")
                    .color("#95E1D3")
                    .build());
            
            categoryRepository.save(Category.builder()
                    .name("일기")
                    .icon("✍️")
                    .color("#F38181")
                    .build());
            
            categoryRepository.save(Category.builder()
                    .name("요리")
                    .icon("🍳")
                    .color("#FFA07A")
                    .build());
            
            categoryRepository.save(Category.builder()
                    .name("공부")
                    .icon("📖")
                    .color("#6C5CE7")
                    .build());
            
            categoryRepository.save(Category.builder()
                    .name("청소")
                    .icon("🧹")
                    .color("#A8E6CF")
                    .build());
            
            categoryRepository.save(Category.builder()
                    .name("기타")
                    .icon("⭐")
                    .color("#FDCB6E")
                    .build());
        }
    }
    
    // 전체 카테고리 조회
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    
    // ID로 카테고리 조회
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));
    }
}
