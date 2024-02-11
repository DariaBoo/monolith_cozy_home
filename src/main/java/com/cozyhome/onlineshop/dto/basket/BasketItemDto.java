package com.cozyhome.onlineshop.dto.basket;

import com.cozyhome.onlineshop.validation.ValidColorHex;
import com.cozyhome.onlineshop.validation.ValidSkuCode;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class BasketItemDto {

	@ValidSkuCode
	private String skuCode;
	
	@ValidColorHex
	private String colorHex;
	
	@Positive(message = "Quantity must be a positive number.")
	private int quantity;
}
