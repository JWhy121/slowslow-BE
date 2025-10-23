package com.elice.slowslow.domain.category.dto;

import com.elice.slowslow.domain.category.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto {
    private Long id;

    private String categoryName;

    public Category toEntity() {
        Category category = new Category();
        category.setId(id);
        category.setCategoryName(categoryName);

        return category;
    }

    public CategoryResponseDto(Category category) {
        this.id = category.getId();
        this.categoryName = category.getCategoryName();
    }
}
