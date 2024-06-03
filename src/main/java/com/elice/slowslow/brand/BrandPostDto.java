package com.elice.slowslow.brand;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
