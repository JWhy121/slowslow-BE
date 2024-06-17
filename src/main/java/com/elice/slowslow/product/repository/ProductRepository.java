package com.elice.slowslow.product.repository;

import com.elice.slowslow.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // productid로 정보를 불러오기 위해 추가
    Page<Product> findById(Long productId, Pageable pageable);

    // 카테고리 ID로 상품 목록 가져오기 (페이징 처리)
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    // 카테고리 ID로 상품 목록 가져오기 (페이징 처리)
    Page<Product> findByBrandId(Long brandId, Pageable pageable);


}