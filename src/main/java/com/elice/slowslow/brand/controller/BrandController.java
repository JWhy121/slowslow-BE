package com.elice.slowslow.brand.controller;


import com.elice.slowslow.brand.*;
import com.elice.slowslow.brand.dto.BrandPostDto;
import com.elice.slowslow.brand.dto.BrandPutDto;
import com.elice.slowslow.brand.dto.BrandResponseDto;
import com.elice.slowslow.brand.repository.BrandRepository;
import com.elice.slowslow.brand.service.BrandService;

import com.elice.slowslow.product.Product;
import com.elice.slowslow.product.dto.ProductDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:3000") // React 개발 서버 주소
public class BrandController {
    private final BrandService brandService;

    private final BrandRepository brandRepository;

    public BrandController(BrandService brandService, BrandRepository brandRepository) {
        this.brandService = brandService;
        this.brandRepository = brandRepository;
    }

    // 브랜드 전체 조회
    @GetMapping("brand/all")
    public ResponseEntity<List<BrandResponseDto>> getAllBrand(Pageable pageable) {
        Page<Brand> brands = brandRepository.findAllByOrderByIdAsc(pageable);
        List<BrandResponseDto> brandResponseDtos = brands.stream()
                .map(Brand::toBrandResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(brandResponseDtos);
    }

    // 특정 브랜드별 전체 상품 조회
    @GetMapping("brand/{brandId}")
    public ResponseEntity<List<ProductDto>> getAllProductByBrand(@PathVariable Long brandId, Pageable pageable) {
        // Brand 조회
        Optional<Brand> brandOptional = brandRepository.findById(brandId);

        // 브랜드가 존재하지 않을 경우
        if (brandOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<ProductDto> products = brandService.getProductsByBrandId(brandId, pageable);
        return ResponseEntity.ok(products);
    }


    // 브랜드 수정 화면
//    @GetMapping("/edit")
//    public String editBrandForm(){
//        // 내부 구현
//        return "브랜드 수정 화면";
//    }

    // 브랜드 수정 화면 - 브랜드 추가
    @PostMapping("admin/brand/post")
    public BrandResponseDto createBrand(@RequestBody BrandPostDto brandPostDto) {
        BrandResponseDto savedBrand = brandService.createBrand(brandPostDto);
        return savedBrand;
    }

    // 브랜드 수정 화면 - 브랜드 수정
    @PostMapping("admin/brand/edit/{brandId}")
    public BrandResponseDto updateBrand(@RequestBody BrandPutDto brandPutDto, @PathVariable Long brandId) {
        Brand brand  = brandService.getBrandById(brandId).toEntity();
        BrandPutDto updatingBrand = new BrandPutDto();
        updatingBrand.setId(brandId);
        updatingBrand.setBrandName(brandPutDto.getBrandName());

        BrandResponseDto updatedBrand = brandService.updateBrand(brandId, updatingBrand);
        return updatedBrand;
    }

    // 브랜드 수정 화면 - 브랜드 삭제
    @DeleteMapping("admin/brand/delete/{brandId}")
    public void deleteBrand(@PathVariable Long brandId){
        Brand brand = brandService.getBrandById(brandId).toEntity();
        brandService.deleteBrand(brand.getId());
    }
}
