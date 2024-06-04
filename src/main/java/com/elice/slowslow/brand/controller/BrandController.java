package com.elice.slowslow.brand;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {
    private final BrandService brandService;

    private final BrandRepository brandRepository;

    public BrandController(BrandService brandService, BrandRepository brandRepository) {
        this.brandService = brandService;
        this.brandRepository = brandRepository;
    }

    // 브랜드 전체 조회
    @GetMapping
    public Page<Brand> getAllBrand(Pageable pageable) {
        // 내부 구현
        Page<Brand> brands = brandRepository.findAllByOrderByIdAsc(pageable);
        return brands;

    }

    // 특정 브랜드별 전체 상품 조회
    @GetMapping("/{brandId}")
    public String getAllProductByBrand(@PathVariable Long brandId, Pageable pageable) {
        // 내부 구현
//        Page<Brand> brands = brandRepository.findAllByOrderByIdAsc(pageable);
//        return brands;
        return "브랜드별 전체 상품 조회";
    }

    // 브랜드 수정 화면
    @GetMapping("/edit")
    public String editBrandForm(){
        // 내부 구현
        return "브랜드 수정 화면";
    }

    // 브랜드 수정 화면 - 브랜드 추가
    @PostMapping("/edit/create")
    public Brand createBrand(@RequestBody BrandPostDto brandPostDto) {
        // 내부 구현
        BrandResponseDto savedBrand = brandService.createBrand(brandPostDto);
        return savedBrand.toEntity();
    }

    // 브랜드 수정 화면 - 브랜드 수정
    @PutMapping("/edit/{postId}")
    public Brand updateBrand(@RequestBody BrandPutDto brandPutDto, @PathVariable Long postId) {
        // 내부 구현
        Brand brand  = brandService.getBrandById(postId).toEntity();
        BrandPutDto updatingBrand = new BrandPutDto();
        updatingBrand.setId(postId);
        updatingBrand.setBrandName(brandPutDto.getBrandName());

        Brand updatedBrand = brandService.updateBrand(postId, updatingBrand).toEntity();
        return updatedBrand;
    }

    // 브랜드 수정 화면 - 브랜드 삭제
    @DeleteMapping("/edit/{postId}")
    public Brand deleteBrand(@PathVariable Long brandId){
        // 내부 구현
        Brand brand = brandService.getBrandById(brandId).toEntity();
        return brandService.deleteBrand(brand.getId()).toEntity();
    }
}
