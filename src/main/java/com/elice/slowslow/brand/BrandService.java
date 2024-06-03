package com.elice.slowslow.brand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrandService {
    private final BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {this.brandRepository = brandRepository;}

    // 전체 브랜드 목록 가져오기

    // 특정 브랜드 전체 상품 가져오기

    // 브랜드 정보 추가하기

    // 브랜드 정보 수정하기

    // 브랜드 정보 삭제하기

}
