package com.cozyhome.onlineshop.dto.productcard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductCardImageDto {
    private String id;
    private boolean mainImage;
    private String desktopImagePath;
    private String sliderImagePath;
    private String mobileImagePath;

}
