package com.elice.slowslow.category.controller;


import com.elice.slowslow.category.*;
import com.elice.slowslow.category.dto.CategoryPostDto;
import com.elice.slowslow.category.dto.CategoryPutDto;
import com.elice.slowslow.category.dto.CategoryResponseDto;
import com.elice.slowslow.category.repository.CategoryRepository;
import com.elice.slowslow.category.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/category")
@CrossOrigin(origins = "http://localhost:3000") // React 개발 서버 주소
public class CategoryController {
    private final CategoryService categoryService;

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryService categoryService, CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    // 카테고리 전체 조회
    @GetMapping("/all")
    public ResponseEntity<List<CategoryResponseDto>> getAllCategory(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAllByOrderByIdAsc(pageable);
        List<CategoryResponseDto> categoryResponseDtos = categories.stream()
                .map(Category::toCategoryResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryResponseDtos);
    }

    // 특정 카테고리별 전체 상품 조회
    @GetMapping("/{categoryId}")
    public String getAllProductByCategory(@PathVariable Long categoryId, Pageable pageable) {
        // 내부 구현
        // Page<Product> products = productRepository.findByAllByCategoryId(categoryId);
        // return products
        return "카테고리별 전체 상품 조회";
    }

    // 카테고리 수정 화면
    @GetMapping("/edit")
    public String editCategoryForm() {
        // 내부 구현
        return "카테고리 수정 화면";
    }

    // 카테고리 수정 화면 - 카테고리 추가
    @PostMapping("/post")
    public CategoryResponseDto createCategory(@RequestBody CategoryPostDto categoryPostDto) {
        CategoryResponseDto savedCategory = categoryService.createCategory(categoryPostDto);
        return savedCategory;
     }

     // 카테고리 수정 화면 - 카테고리 수정
    @PostMapping("/edit/{categoryId}")
    public CategoryResponseDto updateCategory(@PathVariable Long categoryId, @RequestBody CategoryPutDto categoryPutDto)  {
        Category category = categoryService.getCategoryById(categoryId).toEntity();

        CategoryPutDto updatingCategory = new CategoryPutDto();
        updatingCategory.setId(categoryId);
        updatingCategory.setCategoryName(categoryPutDto.getCategoryName());

        CategoryResponseDto updatedCategory = categoryService.updateCategory(categoryId, updatingCategory);
        return updatedCategory;
    }

    // 카레고리 수정 화면 - 카테고리 삭제
    @DeleteMapping("/delete/{categoryId}")
    public void deleteCategory(@PathVariable Long categoryId) {
        Category category = categoryService.getCategoryById(categoryId).toEntity();
        categoryService.deleteCategory(category.getId());
    }
}
