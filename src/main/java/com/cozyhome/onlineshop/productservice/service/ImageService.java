package com.cozyhome.onlineshop.productservice.service;

import com.cozyhome.onlineshop.dto.ImageDto;
import com.cozyhome.onlineshop.dto.PopUpImageDto;
import com.cozyhome.onlineshop.dto.productcard.ProductCardImageDto;
import com.cozyhome.onlineshop.dto.request.ProductColorDto;

import java.util.List;

public interface ImageService {

	ImageDto getPreviewImageForProductByColor(ProductColorDto previewImageProduct);

    List<PopUpImageDto> getPopUpImageForProductByColor(ProductColorDto popUpImageProduct);
    
    List<ProductCardImageDto> getProductCardImagesByColor(ProductColorDto request);
}
