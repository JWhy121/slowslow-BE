package com.elice.slowslow.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByProductId(Long productId, Pageable pageable);
    @Query("SELECT COUNT(p) FROM Post p WHERE p.product.id = :Pid")
    int countPostsByBoardId(Long Pid);

}
