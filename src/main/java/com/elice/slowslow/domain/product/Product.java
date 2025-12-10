package com.elice.slowslow.domain.product;

import com.elice.slowslow.global.audit.BaseEntity;
import com.elice.slowslow.domain.brand.Brand;
import com.elice.slowslow.domain.category.Category;
import com.elice.slowslow.domain.product.dto.ProductDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import jakarta.persistence.*;


@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(name = "image_link", nullable = false)
    private String imageLink;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public void decreaseStock(int quantity) {
        if (this.stock < quantity) {
            throw new IllegalStateException("재고가 부족합니다.");
        }
        this.stock -= quantity;
    }

    public void increaseStock(int quantity) {
        this.stock += quantity;
    }


    public ProductDto toDto(){
        return ProductDto.builder()
                .id(this.id)
                .name(this.name)
                .price(this.price)
                .description(this.description)
                .imageLink(this.imageLink)
                .brandId(this.getBrand().getId())
                .brandName(this.brand.getBrandName())
                .categoryId(this.getCategory().getId())
                .categoryName(this.category.getCategoryName())
                .createdDate(this.getCreatedDate())
                .modifiedDate(this.getModifiedDate())
                .build();
    }

    public static Product fromDto(ProductDto productDto, Brand brand, Category category){
        Product product = Product.builder()
                .id(productDto.getId())
                .name(productDto.getName())
                .price(productDto.getPrice())
                .description(productDto.getDescription())
                .imageLink(productDto.getImageLink())
                .brand(brand)
                .category(category)
                .build();
        product.setCreatedDate(productDto.getCreatedAt());
        product.setModifiedDate(productDto.getModifiedAt());
        return product;

    }


}
