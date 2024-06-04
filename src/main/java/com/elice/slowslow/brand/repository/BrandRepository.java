package com.elice.slowslow.brand.repository;

import com.elice.slowslow.brand.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Page<Brand> findAllByOrderByIdAsc(Pageable pageable);
}
