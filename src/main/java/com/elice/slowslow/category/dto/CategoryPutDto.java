package com.elice.slowslow.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryPutDto {
    private Long id;

    private String categoryName;

    public Category toEntity() {
        Category category = new Category();
        category.setId(id);
        category.setCategoryName(categoryName);

        return category;
    }

    public CategoryPutDto(Category category) {
        this.id = category.getId();
        this.categoryName = category.getCategoryName();
    }
}
