package com.elice.slowslow.category.repository;

import com.elice.slowslow.category.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findAllByOrderByIdAsc(Pageable pageable);
}
