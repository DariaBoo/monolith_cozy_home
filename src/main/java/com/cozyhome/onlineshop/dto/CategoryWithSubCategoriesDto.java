package com.cozyhome.onlineshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CategoryWithSubCategoriesDto {
    private String id;
    private String name;
    private String categoryImagePath;
    private List<CategoryDto> categoryDtos;

}
