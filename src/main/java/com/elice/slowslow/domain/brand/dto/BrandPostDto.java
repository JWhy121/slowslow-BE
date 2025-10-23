package com.elice.slowslow.domain.brand.dto;

import com.elice.slowslow.domain.brand.Brand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrandPostDto {
    private String brandName;

    public Brand toEntity() {
        Brand brand = new Brand();
        brand.setBrandName(brandName);
        return brand;
    }

    public BrandPostDto(Brand brand) {
        this.brandName = brand.getBrandName();
    }
}
