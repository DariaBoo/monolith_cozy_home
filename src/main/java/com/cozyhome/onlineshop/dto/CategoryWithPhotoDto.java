package com.cozyhome.onlineshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CategoryWithPhotoDto {
	private String categoryId;
    private String categoryName;
    private String imagePath;
    private String imageSize;
}
