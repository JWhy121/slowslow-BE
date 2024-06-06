package com.elice.slowslow.category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryPostDto {
    private String categoryName;

    public Category toEntity() {
        Category category = new Category();
        category.setCategoryName(categoryName);
        return category;
    }

    public CategoryPostDto(Category category) {
        this.categoryName = category.getCategoryName();
    }
}
