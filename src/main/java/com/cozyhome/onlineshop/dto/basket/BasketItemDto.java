package com.cozyhome.onlineshop.dto.basket;

import com.cozyhome.onlineshop.validation.ValidColorHex;
import com.cozyhome.onlineshop.validation.ValidSkuCode;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class BasketItemDto {

	@ValidSkuCode
	private String skuCode;
	
	@ValidColorHex
	private String colorHex;
	
	@Min(value = 0, message = "Quantity must be a non-negative number.")
	private int quantity;
}
