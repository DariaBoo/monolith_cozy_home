package com.cozyhome.onlineshop.dto.basket;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BasketDto {

	private String skuCode;
    private String productName;
    private BigDecimal price;
    private BigDecimal priceWithDiscount;
    private String imagePath;
    private String colorHex;
    private String colorName;
    private int quantity;
    private int availableProductQuantity;
    private String quantityStatus;
    private boolean isFavorite;
}
