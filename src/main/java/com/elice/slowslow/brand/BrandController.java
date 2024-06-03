package com.elice.slowslow.brand;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/brand")
public class BrandController {
    private final BrandService brandService;

    private final BrandRepository brandRepository;

    public BrandController(BrandService brandService, BrandRepository brandRepository) {
        this.brandService = brandService;
        this.brandRepository = brandRepository;
    }

    // 브랜드별 전체 상품 조회
    @GetMapping
    public void getAllProductByBrand(@PathVariable Long brandId) {
        // 내부 구현
    }

    // 브랜드 수정 화면
    @GetMapping("/edit")
    public void editBrandForm(){
        // 내부 구현
    }

    // 브랜드 수정 화면 - 브랜드 추가
    @PostMapping("/edit")
    public void createBrand(BrandPostDto brandPostDto){
        // 내부 구현
    }

    // 브랜드 수정 화면 - 브랜드 수정
    @PostMapping("/edit")
    public void updateBrand(BrandPutDto brandPutDto){
        // 내부 구현
    }

    // 브랜드 수정 화면 - 브랜드 삭제
    @DeleteMapping("/edit")
    public void deleteBrand(@PathVariable Long brandId){
        // 내부 구현
    }
}
