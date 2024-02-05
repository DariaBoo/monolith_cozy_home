package com.cozyhome.onlineshop.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FavoriteProductsDto {

	private short countOfProducts;
	
    private short countOfPages;
    
    private List<ProductDto> products = new ArrayList<>();
}
