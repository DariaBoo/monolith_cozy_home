package com.cozyhome.onlineshop.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SearchResultDto {	
	
	private List<ProductSearchDto> products;
	
	private List<CategorySearchDto> categories;
}
