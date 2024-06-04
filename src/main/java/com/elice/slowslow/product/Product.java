package com.elice.slowslow.product;

import lombok.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;




@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Product {
    //제약조건 추가
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long price;

    private String description;

    private String imageLink;

    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "brand_id")
    //private Brand brand;

    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "category_id")
    //private Category category;


    public ProductDto toDto(){
        return ProductDto.builder()
                .id(this.id)
                .name(this.name)
                .price(this.price)
                .description(this.description)
                .imageLink(this.imageLink)
                .build();
    }

    public static Product fromDto(ProductDto productDto){
        return Product.builder()
                .id(productDto.getId())
                .name(productDto.getName())
                .price(productDto.getPrice())
                .description(productDto.getDescription())
                .imageLink(productDto.getImageLink())
                .build();
    }


}
