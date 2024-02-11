package com.cozyhome.onlineshop.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CategorySearchDto {
	
	private String id;
	private String name;
	private Short countOfProducts;
}
