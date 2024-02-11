package com.cozyhome.onlineshop.productservice.service.impl;

import com.cozyhome.onlineshop.dto.ImageDto;
import com.cozyhome.onlineshop.dto.PopUpImageDto;
import com.cozyhome.onlineshop.dto.productcard.ProductCardImageDto;
import com.cozyhome.onlineshop.dto.request.ProductColorDto;
import com.cozyhome.onlineshop.exception.DataNotFoundException;
import com.cozyhome.onlineshop.productservice.model.ImageProduct;
import com.cozyhome.onlineshop.productservice.repository.ImageProductRepository;
import com.cozyhome.onlineshop.productservice.service.ImageService;
import com.cozyhome.onlineshop.productservice.service.builder.ImageBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
	private final ImageProductRepository imageProductRepository;
	private final ImageBuilder imageBuilder;

    @Override
    public ImageDto getPreviewImageForProductByColor(ProductColorDto productColor) {
        ImageProduct image = imageProductRepository.findByProductSkuCodeAndColorIdAndMainPhotoTrue(
                productColor.getProductSkuCode(), productColor.getColorHex())
				.orElseThrow(() -> new DataNotFoundException(String.format("Image for product with skuCode %s and " +
						"color hex %s doesn't found", productColor.getProductSkuCode(), productColor.getColorHex())));
        return imageBuilder.buildPreviewImageDto(image);
    }

	@Override
	public List<PopUpImageDto> getPopUpImageForProductByColor(ProductColorDto productColor) {
		List<ImageProduct> popUpImages = imageProductRepository
				.findByProductSkuCodeAndColorId(productColor.getProductSkuCode(), productColor.getColorHex());
		if (popUpImages.isEmpty()) {
			log.info("[ON getPopUpImageForProductByColor]:: Image for product with skuCode {} and color hex {} " +
					"doesn't found.", productColor.getProductSkuCode(), productColor.getColorHex());
			return new ArrayList<>();
		}
		return imageBuilder.buildPopUpImageDtos(popUpImages);
	}

	@Override
	public List<ProductCardImageDto> getProductCardImagesByColor(ProductColorDto productColor) {
		List<ImageProduct> productCardImages = imageProductRepository
				.findByProductSkuCodeAndColorId(productColor.getProductSkuCode(), productColor.getColorHex());
		if (productCardImages.isEmpty()) {
			log.info("[ON getProductCardImagesByColor]:: Image for product with skuCode {} and color hex {} " +
					"doesn't found.", productColor.getProductSkuCode(), productColor.getColorHex());
			return new ArrayList<>();
		}
		return imageBuilder.buildProductCardImageDtos(productCardImages);
	}
}
