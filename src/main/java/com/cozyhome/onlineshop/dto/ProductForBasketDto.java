package com.cozyhome.onlineshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductForBasketDto {
    private String skuCode;
    private String name;
    private BigDecimal price;
    private BigDecimal priceWithDiscount;
    private String imagePath;
    private String colorName;
    private String colorHex;
    private int availableProductQuantity;
    private String quantityStatus;
}
