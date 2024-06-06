package com.elice.slowslow.category.controller;

import com.elice.slowslow.category.Category;
import com.elice.slowslow.category.dto.CategoryPostDto;
import com.elice.slowslow.category.dto.CategoryPutDto;
import com.elice.slowslow.category.dto.CategoryResponseDto;
import com.elice.slowslow.category.repository.CategoryRepository;
import com.elice.slowslow.category.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryService categoryService, CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    // 카테고리 전체 조회
    @GetMapping
    public Page<Category> getAllCategory(Pageable pageable) {
        // 내부 구현
        Page<Category> categories = categoryRepository.findAllByOrderByIdAsc(pageable);
        return categories;
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
    @PostMapping("/edit")
    public Category createCategory(@RequestBody CategoryPostDto categoryPostDto) {
        CategoryResponseDto savedCategory = categoryService.createCategory(categoryPostDto);
        return savedCategory.toEntity();
     }

     // 카테고리 수정 화면 - 카테고리 수정
    @PostMapping("/edit/{categoryId}")
    public Category updateCategory(@RequestBody CategoryPutDto categoryPutDto, @PathVariable Long categoryId)  {
        Category category = categoryService.getCategoryById(categoryId).toEntity();

        CategoryPutDto updatingCategory = new CategoryPutDto();
        updatingCategory.setId(categoryId);
        updatingCategory.setCategoryName(categoryPutDto.getCategoryName());

        Category updatedCategory = categoryService.updateCategory(categoryId, updatingCategory).toEntity();
        return updatedCategory;
    }

    // 카레고리 수정 화면 - 카테고리 삭제
    @DeleteMapping("/edit/{categoryId}")
    public void deleteCategory(@PathVariable Long categoryId) {
        Category category = categoryService.getCategoryById(categoryId).toEntity();
        categoryService.deleteCategory(category.getId());
    }
}
