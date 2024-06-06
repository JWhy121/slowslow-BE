package com.elice.slowslow.category.dto;

import com.elice.slowslow.category.Category;
import com.elice.slowslow.category.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
