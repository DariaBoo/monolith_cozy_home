package com.cozyhome.onlineshop.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductSearchDto {
	
	private String skuCode;
	private String name;
	private String imagePath;
	private String colorHex;
	private BigDecimal price;
	private BigDecimal priceWithDiscount;
}
