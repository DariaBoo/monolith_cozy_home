package com.cozyhome.onlineshop.dto;

import com.cozyhome.onlineshop.dto.productcard.ColorQuantityStatusDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductDto {
	
    private String skuCode;
    private String name;
    private String shortDescription;
    private BigDecimal price;
    private Byte discount;
    private BigDecimal priceWithDiscount;
    private List<ImageDto> imageDtoList;
    private List<ColorQuantityStatusDto> colorDtoList;
    private String productQuantityStatus;
    private boolean isFavorite;
}
