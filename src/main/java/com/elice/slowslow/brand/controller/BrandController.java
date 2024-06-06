package com.elice.slowslow.brand.controller;


import com.elice.slowslow.brand.*;
import com.elice.slowslow.brand.dto.BrandPostDto;
import com.elice.slowslow.brand.dto.BrandPutDto;
import com.elice.slowslow.brand.dto.BrandResponseDto;
import com.elice.slowslow.brand.repository.BrandRepository;
import com.elice.slowslow.brand.service.BrandService;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

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
        // Page<Product> products = productRepository.findByAllByBrandId(brandId);
        // return products
        return "브랜드별 전체 상품 조회";
    }

    // 브랜드 수정 화면
    @GetMapping("/edit")
    public String editBrandForm(){
        // 내부 구현
        return "브랜드 수정 화면";
    }

    // 브랜드 수정 화면 - 브랜드 추가
    @PostMapping("/edit")
    public Brand createBrand(@RequestBody BrandPostDto brandPostDto) {
        BrandResponseDto savedBrand = brandService.createBrand(brandPostDto);
        return savedBrand.toEntity();
    }

    // 브랜드 수정 화면 - 브랜드 수정
    @PutMapping("/edit/{brandId}")
    public Brand updateBrand(@RequestBody BrandPutDto brandPutDto, @PathVariable Long brandId) {
        Brand brand  = brandService.getBrandById(brandId).toEntity();
        BrandPutDto updatingBrand = new BrandPutDto();
        updatingBrand.setId(brandId);
        updatingBrand.setBrandName(brandPutDto.getBrandName());

        Brand updatedBrand = brandService.updateBrand(brandId, updatingBrand).toEntity();
        return updatedBrand;
    }

    // 브랜드 수정 화면 - 브랜드 삭제
    @DeleteMapping("/edit/{brandId}")
    public void deleteBrand(@PathVariable Long brandId){
        Brand brand = brandService.getBrandById(brandId).toEntity();
        brandService.deleteBrand(brand.getId());
    }
}
