package com.cozyhome.onlineshop.dto.request;

import com.cozyhome.onlineshop.validation.ValidColorHex;
import com.cozyhome.onlineshop.validation.ValidSkuCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductColorDto {

    @ValidSkuCode
    private String productSkuCode;
    @ValidColorHex
    private String colorHex;
}
