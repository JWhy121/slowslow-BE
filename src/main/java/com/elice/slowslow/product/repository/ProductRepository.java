package com.elice.slowslow.product.repository;

import com.elice.slowslow.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // productid로 정보를 불러오기 위해 추가
    Page<Product> findById(Long productId, Pageable pageable);



}