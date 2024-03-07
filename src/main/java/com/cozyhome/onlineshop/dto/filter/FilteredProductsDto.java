package com.cozyhome.onlineshop.dto.filter;

import java.util.List;

import com.cozyhome.onlineshop.dto.ProductDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FilteredProductsDto {

	private String keyWord;
	private List<ProductDto> filteredProducts;
}
