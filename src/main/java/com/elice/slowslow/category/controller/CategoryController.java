package com.elice.slowslow.category.controller;


import com.elice.slowslow.brand.Brand;
import com.elice.slowslow.category.*;
import com.elice.slowslow.category.dto.CategoryPostDto;
import com.elice.slowslow.category.dto.CategoryPutDto;
import com.elice.slowslow.category.dto.CategoryResponseDto;
import com.elice.slowslow.category.repository.CategoryRepository;
import com.elice.slowslow.category.service.CategoryService;
import com.elice.slowslow.product.dto.ProductDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:3000") // React 개발 서버 주소
public class CategoryController {
    private final CategoryService categoryService;

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryService categoryService, CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    // 카테고리 전체 조회
    @GetMapping("category/all")
    public ResponseEntity<List<CategoryResponseDto>> getAllCategory(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAllByOrderByIdAsc(pageable);
        List<CategoryResponseDto> categoryResponseDtos = categories.stream()
                .map(Category::toCategoryResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryResponseDtos);
    }

    // 특정 카테고리별 전체 상품 조회
    // 특정 브랜드별 전체 상품 조회
    @GetMapping("category/{categoryId}")
    public ResponseEntity<Page<ProductDto>> getAllProductByCategory(@PathVariable Long categoryId, Pageable pageable) {
        // Brand 조회
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);

        // 브랜드가 존재하지 않을 경우
        if (categoryOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Page<ProductDto> products = categoryService.getProductsByCategoryId(categoryId, pageable);
        return ResponseEntity.ok(products);
    }

    // 카테고리 수정 화면
//    @GetMapping("/edit")
//    public String editCategoryForm() {
//        // 내부 구현
//        return "카테고리 수정 화면";
//    }

    // 카테고리 수정 화면 - 카테고리 추가
    @PostMapping("admin/category/post")
    public CategoryResponseDto createCategory(@RequestBody CategoryPostDto categoryPostDto) {
        CategoryResponseDto savedCategory = categoryService.createCategory(categoryPostDto);
        return savedCategory;
     }

     // 카테고리 수정 화면 - 카테고리 수정
    @PostMapping("admin/category/edit/{categoryId}")
    public CategoryResponseDto updateCategory(@PathVariable Long categoryId, @RequestBody CategoryPutDto categoryPutDto)  {
        Category category = categoryService.getCategoryById(categoryId).toEntity();

        CategoryPutDto updatingCategory = new CategoryPutDto();
        updatingCategory.setId(categoryId);
        updatingCategory.setCategoryName(categoryPutDto.getCategoryName());

        CategoryResponseDto updatedCategory = categoryService.updateCategory(categoryId, updatingCategory);
        return updatedCategory;
    }

    // 카레고리 수정 화면 - 카테고리 삭제
    @DeleteMapping("admin/category/delete/{categoryId}")
    public void deleteCategory(@PathVariable Long categoryId) {
        Category category = categoryService.getCategoryById(categoryId).toEntity();
        categoryService.deleteCategory(category.getId());
    }
}
