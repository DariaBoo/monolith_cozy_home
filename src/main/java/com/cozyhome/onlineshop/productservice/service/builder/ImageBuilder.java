package com.cozyhome.onlineshop.productservice.service.builder;

import com.cozyhome.onlineshop.dto.ImageDto;
import com.cozyhome.onlineshop.dto.PopUpImageDto;
import com.cozyhome.onlineshop.dto.productcard.ProductCardImageDto;
import com.cozyhome.onlineshop.productservice.model.ImageProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ImageBuilder {
    @Value("${image.product.path.base}")
    private String imagePathBase;

    public List<ImageDto> buildImageDtoList(List<ImageProduct> images) {
        if (images == null || images.isEmpty()) {
            return Collections.emptyList();
        }
        return images.stream().map(this::buildPreviewImageDto).toList();
    }

    public ImageDto buildPreviewImageDto(ImageProduct image) {
        return ImageDto.builder()
            .id(image.getId().toString())
            .imagePath(imagePathBase + image.getPreviewImageName())
            .color(image.getColor().getName())
            .build();
    }

    public List<PopUpImageDto> buildPopUpImageDtos(List<ImageProduct> images) {
        return images.stream().map(this::buildPopUpImageDto).toList();
    }

    public PopUpImageDto buildPopUpImageDto(ImageProduct image) {
        return PopUpImageDto.builder()
            .id(image.getId().toString())
            .desktopPopUpImagePath(imagePathBase + image.getPopUpImageName())
            .mobilePopUpImagePath(imagePathBase + image.getMobileImageName())
            .build();
    }

    public List<ProductCardImageDto> buildProductCardImageDtos(List<ImageProduct> images) {
        return images.stream().map(this::buildProductCardImageDto).toList();
    }

    public ProductCardImageDto buildProductCardImageDto(ImageProduct image) {
    	return ProductCardImageDto.builder()
    			.id(image.getId().toString())
    			.mainImage(image.isMainPhoto())
    			.desktopImagePath(imagePathBase + image.getDesktopImageName())
    			.sliderImagePath(imagePathBase + image.getSliderImageName())
    			.mobileImagePath(imagePathBase + image.getMobileImageName())
    			.build();
    }
}
