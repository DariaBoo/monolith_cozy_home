package com.cozyhome.onlineshop.basketservice.service.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cozyhome.onlineshop.basketservice.model.BasketItem;
import com.cozyhome.onlineshop.dto.basket.BasketDto;
import com.cozyhome.onlineshop.dto.basket.BasketItemDto;
import com.cozyhome.onlineshop.dto.inventory.ProductAvailabilityDto;
import com.cozyhome.onlineshop.dto.request.ProductColorDto;
import com.cozyhome.onlineshop.exception.DataNotFoundException;
import com.cozyhome.onlineshop.inventoryservice.model.ProductColor;
import com.cozyhome.onlineshop.inventoryservice.repository.ProductColorRepository;
import com.cozyhome.onlineshop.inventoryservice.service.InventoryService;
import com.cozyhome.onlineshop.productservice.model.ImageProduct;
import com.cozyhome.onlineshop.productservice.model.Product;
import com.cozyhome.onlineshop.productservice.model.enums.ColorsEnum;
import com.cozyhome.onlineshop.productservice.repository.ImageRepositoryCustom;
import com.cozyhome.onlineshop.productservice.repository.ProductRepository;
import com.cozyhome.onlineshop.userservice.model.FavoriteProduct;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class BasketBuilder {

	private final ProductRepository productRepository;
	private final ImageRepositoryCustom imageRepositoryCustom;
	private final ProductColorRepository productColorRepository;
	private final InventoryService inventoryService;

	private final ModelMapper modelMapper;

	@Value("${image.product.path.base}")
	private String imagePathBase;

	public List<BasketDto> buildBasketDtoList(List<BasketItem> basketItemList,
			List<FavoriteProduct> favoriteProductList) {
		Map<ProductColorDto, ImageProduct> imageMap = getImageMap(basketItemList);
		List<ProductColorDto> productColorDtoList = basketItemList.parallelStream()
				.map(basketItem -> modelMapper.map(basketItem.getProductColor(), ProductColorDto.class)).toList();
		List<ProductAvailabilityDto> inventoryForBasketList = inventoryService
				.getProductAvailableStatus(productColorDtoList);
		List<BasketDto> basketDtoList = new ArrayList<>();
		for (BasketItem basketItem : basketItemList) {
			ProductColorDto productColorDto = modelMapper.map(basketItem.getProductColor(), ProductColorDto.class);
			ProductAvailabilityDto inventory = inventoryForBasketList.stream()
					.filter(inventoryDto -> inventoryDto.getProductColorDto().equals(productColorDto)).findAny()
					.orElseThrow(() -> new DataNotFoundException("No available quantity found"));
			BasketDto dto = buildBasketDto(basketItem, imageMap.get(productColorDto), inventory);
			boolean isFavorite = favoriteProductList.stream()
					.anyMatch(item -> item.getProductSkuCode().equals(basketItem.getProductColor().getProductSkuCode()));
			if (isFavorite) {
				dto.setFavorite(isFavorite);
			}
			basketDtoList.add(dto);
		}
		return basketDtoList;
	}

	private Map<ProductColorDto, ImageProduct> getImageMap(List<BasketItem> list) {
		List<ProductColorDto> dtos = list.stream()
				.map(line -> modelMapper.map(line.getProductColor(), ProductColorDto.class)).toList();
		return imageRepositoryCustom.findMainImagesByProductColorList(dtos);
	}

	private BasketDto buildBasketDto(BasketItem basketItem, ImageProduct imageProduct,
			ProductAvailabilityDto productAvailabilityDto) {
		Product product = productRepository.findBySkuCode(basketItem.getProductColor().getProductSkuCode())
				.orElseThrow(() -> new IllegalArgumentException(
						"No product found by skuCode " + basketItem.getProductColor().getProductSkuCode()));

		String imagePah = imagePathBase + imageProduct.getSliderImageName();
		int availableProductQuantity = productAvailabilityDto.getAvailabilityStatusDto().getAvailableProductQuantity();
		int productQuantity = basketItem.getQuantity();
		BasketDto dto = BasketDto.builder().skuCode(product.getSkuCode()).productName(product.getName())
				.price(product.getPrice()).imagePath(imagePah).colorHex(basketItem.getProductColor().getColorHex())
				.colorName(ColorsEnum.getColorNameByHex(basketItem.getProductColor().getColorHex()))
				.availableProductQuantity(availableProductQuantity)
				.quantityStatus(productAvailabilityDto.getAvailabilityStatusDto().getQuantityStatus()).build();
		
		if(productQuantity > availableProductQuantity) {
			dto.setQuantity(availableProductQuantity);
		} else {
			dto.setQuantity(productQuantity);
		}
		
		if (product.getDiscount() > 0) {
			dto.setPriceWithDiscount(product.getPriceWithDiscount());
		}
		return dto;
	}

	public List<BasketItem> buildBasketItemList(String userId, List<BasketItemDto> dtoList) {
		List<BasketItem> result = new ArrayList<>();
		for (BasketItemDto dto : dtoList) {
			ProductColor productColor = productColorRepository
					.findByProductSkuCodeAndColorHex(dto.getSkuCode(), dto.getColorHex())
					.orElseThrow(() -> new IllegalArgumentException("No productColor found."));
			BasketItem basketItem = BasketItem.builder()
					.productColor(productColor)
					.quantity(dto.getQuantity())
					.userId(userId).build();
			result.add(basketItem);
		}
		return result;
	}
}
