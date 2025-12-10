package com.elice.slowslow.domain.category.service;

import com.elice.slowslow.domain.category.Category;
import com.elice.slowslow.domain.category.dto.CategoryPostDto;
import com.elice.slowslow.domain.category.dto.CategoryPutDto;
import com.elice.slowslow.domain.category.dto.CategoryResponseDto;
import com.elice.slowslow.domain.category.repository.CategoryRepository;
import com.elice.slowslow.domain.product.Product;
import com.elice.slowslow.domain.product.dto.ProductDto;
import com.elice.slowslow.domain.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    // 전체 카테고리 목록 가져오기
    public List<CategoryResponseDto> getAllCategory() {
        return ((List<Category>) categoryRepository.findAll()).stream()
                .map(CategoryResponseDto::new)
                .collect(Collectors.toList());
    }

    // 특정 카테고리 가져오기
    public CategoryResponseDto getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(CategoryResponseDto::new)
                .orElseThrow(() -> new IllegalStateException("Category with id: " + id + "does not exist"));
    }

    // 특정 카테고리 상품 목록 가져오기
    public Page<ProductDto> getProductsByCategoryId(Long categoryId, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalStateException("Category with id: " + categoryId + " does not exist"));
        return productRepository.findByCategory(category, pageable)
                .map(Product::toDto);
    }

    // DTO로 정보 전달을 위한 메서드 추가
    private CategoryResponseDto converToDto(Category category) {return new CategoryResponseDto(category);}

    // 카테고리 정보 추가하기
    @Transactional
    public CategoryResponseDto createCategory(CategoryPostDto categoryPostDto) {
        Category category = new Category();
        category.setCategoryName(categoryPostDto.getCategoryName());

        Category savedCategory = categoryRepository.save(category);
        return converToDto(savedCategory);
    }

    // 카테고리 정보 수정하기
    public CategoryResponseDto updateCategory(Long id, CategoryPutDto categoryPutDto) {
        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setCategoryName(categoryPutDto.getCategoryName());
                    Category updateCategory = categoryRepository.save(existingCategory);
                    return updateCategory.toCategoryResponseDto();
                })
                .orElseThrow(() -> new IllegalStateException("Category with id: " + id + "does not exist."));
    }

    // 카테고리 정보 삭제하기
    public CategoryResponseDto deleteCategory(Long id) {
        categoryRepository.deleteById(id);
        return null;
    }
}
