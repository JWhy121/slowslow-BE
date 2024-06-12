package com.elice.slowslow.brand.controller;


import com.elice.slowslow.brand.*;
import com.elice.slowslow.brand.dto.BrandPostDto;
import com.elice.slowslow.brand.dto.BrandPutDto;
import com.elice.slowslow.brand.dto.BrandResponseDto;
import com.elice.slowslow.brand.repository.BrandRepository;
import com.elice.slowslow.brand.service.BrandService;

import com.elice.slowslow.product.Product;
import com.elice.slowslow.product.ProductDto;
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
@RequestMapping("/brand")
@CrossOrigin(origins = "http://localhost:3000") // React 개발 서버 주소
public class BrandController {
    private final BrandService brandService;

    private final BrandRepository brandRepository;

    public BrandController(BrandService brandService, BrandRepository brandRepository) {
        this.brandService = brandService;
        this.brandRepository = brandRepository;
    }

    // 브랜드 전체 조회
    @GetMapping("/all")
    public ResponseEntity<List<BrandResponseDto>> getAllBrand(Pageable pageable) {
        Page<Brand> brands = brandRepository.findAllByOrderByIdAsc(pageable);
        List<BrandResponseDto> brandResponseDtos = brands.stream()
                .map(Brand::toBrandResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(brandResponseDtos);
    }

    // 특정 브랜드별 전체 상품 조회
    @GetMapping("/{brandId}")
    public ResponseEntity<BrandResponseDto> getAllProductByBrand(@PathVariable Long brandId, Pageable pageable) {
        // Brand 조회
        Optional<Brand> brandOptional = brandRepository.findById(brandId);

        // 브랜드가 존재하지 않을 경우
        if (brandOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Brand brand = brandOptional.get();
        BrandResponseDto responseDto = brand.toBrandResponseDto();

//        // 브랜드에 해당하는 제품 리스트를 페이지네이션 처리하여 가져오기
//        Page<Product> products = brandRepository.findAllProductsByBrandId(brandId, pageable);
//        List<ProductDto> productDtos = products.stream()
//                .map(product -> new ProductDto(product.getId(), product.getName(), product.getPrice(), product.getDescription(), product.getImageLink()))
//                .collect(Collectors.toList());


        return ResponseEntity.ok(responseDto);
    }

    // 브랜드 수정 화면
    @GetMapping("/edit")
    public String editBrandForm(){
        // 내부 구현
        return "브랜드 수정 화면";
    }

    // 브랜드 수정 화면 - 브랜드 추가
    @PostMapping("/post")
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
    @DeleteMapping("/delete/{brandId}")
    public void deleteBrand(@PathVariable Long brandId){
        Brand brand = brandService.getBrandById(brandId).toEntity();
        brandService.deleteBrand(brand.getId());
    }
}
