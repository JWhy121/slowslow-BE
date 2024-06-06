package com.elice.slowslow.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {this.categoryRepository = categoryRepository;}

    // 전체 카테고리 목록 가져오기

    // 특정 카테고리 전체 상품 가져오기

    // 카테고리 정보 추가하기

    // 카테고리 정보 수정하기

    // 카테고리 정보 삭제하기

}
