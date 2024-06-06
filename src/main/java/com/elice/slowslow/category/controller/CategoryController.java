package com.elice.slowslow.category;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryService categoryService, CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    // 카테고리별 전체 상품 조회
    @GetMapping
    public void getAllProductByCategory(@PathVariable Long categoryId) {
        // 내부 구현
    }

    // 카테고리 수정 화면
    @GetMapping("/edit")
    public void editCategoryForm() {
        // 내부 구현
    }

    // 카테고리 수정 화면 - 카테고리 추가
    @PostMapping("/edit")
    public void createCategory(CategoryPostDto categoryPostDto) {
        // 내부 구현
     }

     // 카테고리 수정 화면 - 카테고리 수정
    @PostMapping("/edit")
    public void updateCategory(CategoryPutDto categoryPutDto)  {
        // 내부 구현
    }

    // 카레고리 수정 화면 - 카테고리 삭제
    public void deleteCategory(@PathVariable Long categoryId) {
        // 내부 구현
    }
}
