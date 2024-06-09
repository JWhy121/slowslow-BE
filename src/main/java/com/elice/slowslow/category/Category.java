package com.elice.slowslow.category;

import com.elice.slowslow.audit.BaseEntity;
import com.elice.slowslow.category.dto.CategoryPutDto;
import com.elice.slowslow.category.dto.CategoryResponseDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String categoryName;

    public CategoryPutDto toCategoryPutDto() {
        CategoryPutDto categoryPutDto = new CategoryPutDto();

        categoryPutDto.setId(this.getId());
        categoryPutDto.setCategoryName(this.getCategoryName());
        return categoryPutDto;
    }

    public CategoryResponseDto toCategoryResponseDto() {
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();

        categoryResponseDto.setId(this.getId());
        categoryResponseDto.setCategoryName(this.getCategoryName());

        return categoryResponseDto;
    }
}
