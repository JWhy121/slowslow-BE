package com.elice.slowslow.product;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private Long price;
    private String description;
    private String imageLink;

    @Builder
    public ProductDto(Long id, String name, Long price, String description, String imageLink) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageLink = imageLink;

    }


}
