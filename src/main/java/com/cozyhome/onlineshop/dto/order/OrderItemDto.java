package com.cozyhome.onlineshop.dto.order;

import com.cozyhome.onlineshop.validation.ValidColorHex;
import com.cozyhome.onlineshop.validation.ValidQuantity;
import com.cozyhome.onlineshop.validation.ValidSkuCode;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemDto {
    @ValidSkuCode
    private String productSkuCode;
    @ValidColorHex
    private String colorHex;
    @ValidQuantity
    private short quantity;
    @DecimalMin(value = "0", message = "Price of product must be greater than 0.")
    @NotBlank(message = "Price of product must be greater than 0.")
    private String price;
}
