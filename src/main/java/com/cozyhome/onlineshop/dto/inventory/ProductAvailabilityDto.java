package com.cozyhome.onlineshop.dto.inventory;

import com.cozyhome.onlineshop.dto.request.ProductColorDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductAvailabilityDto {
	
    private ProductColorDto productColorDto;
    
    private AvailabilityStatusDto availabilityStatusDto;
}
