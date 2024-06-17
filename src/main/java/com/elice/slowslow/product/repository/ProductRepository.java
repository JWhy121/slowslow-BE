package com.elice.slowslow.product.repository;

import com.elice.slowslow.brand.Brand;
import com.elice.slowslow.category.Category;
import com.elice.slowslow.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // productid로 정보를 불러오기 위해 추가
    Page<Product> findById(Long productId, Pageable pageable);

    // 카테고리 ID로 상품 목록 가져오기 (페이징 처리)
    // Category 객체를 이용하여 제품 목록을 찾기 위한 메서드
    Page<Product> findByCategory(Category category, Pageable pageable);

    // 카테고리 ID로 상품 목록 가져오기 (페이징 처리)
    Page<Product> findByBrand(Brand brand, Pageable pageable);


}