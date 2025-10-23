package com.elice.slowslow.domain.brand.service;

import com.elice.slowslow.domain.brand.Brand;
import com.elice.slowslow.domain.brand.repository.BrandRepository;
import com.elice.slowslow.domain.brand.dto.BrandPostDto;
import com.elice.slowslow.domain.brand.dto.BrandPutDto;
import com.elice.slowslow.domain.brand.dto.BrandResponseDto;
import com.elice.slowslow.domain.product.Product;
import com.elice.slowslow.domain.product.dto.ProductDto;
import com.elice.slowslow.domain.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandService {
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository, ProductRepository productRepository) {
        this.brandRepository = brandRepository;
        this.productRepository = productRepository;
    }

    // 전체 브랜드 목록 가져오기
    public List<BrandResponseDto> getAllBrand() {
        return ((List<Brand>) brandRepository.findAll()).stream()
                .map(BrandResponseDto::new)
                .collect(Collectors.toList());
    }

    // 특정 브랜드 가져오기
    public BrandResponseDto getBrandById(Long id) {
        return brandRepository.findById(id)
                .map(BrandResponseDto::new)
                .orElseThrow(() -> new IllegalStateException("Brand with id: " + id + "does not exist"));
    }

    // 특정 브랜드 상품 목록 가져오기
    public Page<ProductDto> getProductsByBrandId(Long brandId, Pageable pageable) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new IllegalStateException("Brand with id: " + brandId + " does not exist"));
        return productRepository.findByBrand(brand, pageable)
                .map(Product::toDto);
    }

    // DTO로 정보 전달을 위한 메서드 추가
    private BrandResponseDto converToDto(Brand brand) {return new BrandResponseDto(brand);}

    // 브랜드 정보 추가하기
    @Transactional
    public BrandResponseDto createBrand(BrandPostDto brandPostDto) {
        Brand brand = new Brand();
        brand.setBrandName(brandPostDto.getBrandName());

        Brand savedBrand = brandRepository.save(brand);
        return converToDto(savedBrand);
    }

    // 브랜드 정보 수정하기
    public BrandResponseDto updateBrand(Long id, BrandPutDto brandPutDto) {
        return brandRepository.findById(id)
                .map(existingBrand -> {
                    existingBrand.setBrandName(brandPutDto.getBrandName());
                    Brand updateBrand = brandRepository.save(existingBrand);
                    return updateBrand.toBrandResponseDto();
                })
                .orElseThrow(() -> new IllegalStateException("Brand with id: " + id + "does not exist."));
    }

    // 브랜드 정보 삭제하기
    public BrandResponseDto deleteBrand(Long id) {
        brandRepository.deleteById(id);
        return null;
    }
}
