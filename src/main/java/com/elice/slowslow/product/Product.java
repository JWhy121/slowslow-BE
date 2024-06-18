package com.elice.slowslow.product;

import com.elice.slowslow.audit.BaseEntity;
import com.elice.slowslow.brand.Brand;
import com.elice.slowslow.category.Category;
import com.elice.slowslow.product.dto.ProductDto;
import lombok.*;
import jakarta.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Product extends BaseEntity {
    //제약조건 추가
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "imageLink", nullable = false)
    private String imageLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;


    public ProductDto toDto(){
        return ProductDto.builder()
                .id(this.id)
                .name(this.name)
                .price(this.price)
                .description(this.description)
                .imageLink(this.imageLink)
                .brandId(this.getBrand().getId())
                .categoryId(this.getCategory().getId())
                .build();
    }

    public static Product fromDto(ProductDto productDto, Brand brand, Category category){
        return Product.builder()
                .id(productDto.getId())
                .name(productDto.getName())
                .price(productDto.getPrice())
                .description(productDto.getDescription())
                .imageLink(productDto.getImageLink())
                .brand(brand)
                .category(category)
                .build();

    }


}
