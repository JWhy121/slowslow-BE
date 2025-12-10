package com.elice.slowslow.domain.brand;

import com.elice.slowslow.domain.brand.dto.BrandPutDto;
import com.elice.slowslow.domain.brand.dto.BrandResponseDto;
import com.elice.slowslow.global.audit.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;


@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Brand extends BaseEntity {
    // Product와 연결
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String brandName;

    public BrandPutDto toBrandPutDto() {
        BrandPutDto brandPutDto = new BrandPutDto();
        brandPutDto.setId(this.getId());
        brandPutDto.setBrandName(this.getBrandName());
        return brandPutDto;
    }

    public BrandResponseDto toBrandResponseDto() {
        BrandResponseDto brandResponseDto = new BrandResponseDto();
        brandResponseDto.setId(this.getId());
        brandResponseDto.setBrandName(this.getBrandName());
        return brandResponseDto;
    }
}
