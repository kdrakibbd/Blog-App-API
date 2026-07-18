package com.rakib.blog.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryResponse {
    private Integer categoryId;
    private String categoryTitle;
    private String categoryDescription;
}
