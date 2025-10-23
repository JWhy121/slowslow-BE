package com.elice.slowslow.domain.product.repository;

import com.elice.slowslow.domain.brand.Brand;
import com.elice.slowslow.domain.category.Category;
import com.elice.slowslow.domain.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // productid로 정보를 불러오기 위해 추가
    Page<Product> findById(Long productId, Pageable pageable);

    // 카테고리 상품 목록 가져오기 (페이징 처리)
    // Category 객체를 이용하여 제품 목록을 찾기 위한 메서드
    Page<Product> findByCategory(Category category, Pageable pageable);

    // 브랜드 상품 목록 가져오기 (페이징 처리)
    Page<Product> findByBrand(Brand brand, Pageable pageable);

    //최신순, 최근 수정한 상품 4개씩 가져오게
    List<Product> findTop4ByOrderByCreatedAtDesc();

    List<Product> findTop4ByOrderByModifiedAtDesc();


}