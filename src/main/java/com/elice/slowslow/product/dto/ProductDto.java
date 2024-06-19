package com.elice.slowslow.product.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private Long price;
    private String description;
    private String imageLink;
    private Long brandId;
    private String brandName;
    private Long categoryId;
    private String categoryName;

    @Builder
    public ProductDto(Long id, String name, Long price, String description,
                      String imageLink, Long brandId, Long categoryId,
                      String brandName, String categoryName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageLink = imageLink;
        this.brandId = brandId;
        this.brandName = brandName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;

    }


}

