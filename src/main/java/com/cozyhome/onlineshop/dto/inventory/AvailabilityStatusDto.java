package com.cozyhome.onlineshop.dto.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AvailabilityStatusDto {
	
    private int availableProductQuantity;
    
    private String quantityStatus;
}
