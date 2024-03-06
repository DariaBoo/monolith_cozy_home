package com.cozyhome.onlineshop.productservice.service.builder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cozyhome.onlineshop.dto.CategorySearchDto;
import com.cozyhome.onlineshop.dto.CollectionDto;
import com.cozyhome.onlineshop.dto.ProductDto;
import com.cozyhome.onlineshop.dto.ProductForBasketDto;
import com.cozyhome.onlineshop.dto.ProductSearchDto;
import com.cozyhome.onlineshop.dto.SearchResultDto;
import com.cozyhome.onlineshop.dto.inventory.AvailabilityStatusDto;
import com.cozyhome.onlineshop.dto.inventory.QuantityStatusDto;
import com.cozyhome.onlineshop.dto.productcard.ColorQuantityStatusDto;
import com.cozyhome.onlineshop.dto.productcard.ProductCardDto;
import com.cozyhome.onlineshop.dto.request.ProductColorDto;
import com.cozyhome.onlineshop.dto.review.ReviewResponse;
import com.cozyhome.onlineshop.exception.DataNotFoundException;
import com.cozyhome.onlineshop.inventoryservice.service.InventoryService;
import com.cozyhome.onlineshop.productservice.model.Category;
import com.cozyhome.onlineshop.productservice.model.ImageProduct;
import com.cozyhome.onlineshop.productservice.model.Material;
import com.cozyhome.onlineshop.productservice.model.Product;
import com.cozyhome.onlineshop.productservice.model.enums.ColorsEnum;
import com.cozyhome.onlineshop.productservice.repository.CategoryRepository;
import com.cozyhome.onlineshop.productservice.repository.ImageProductRepository;
import com.cozyhome.onlineshop.productservice.repository.ImageRepositoryCustom;
import com.cozyhome.onlineshop.reviewservice.service.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductBuilder {
	private final ImageBuilder imageBuilder;
	private final ModelMapper modelMapper;
	private final CategoryRepository categoryRepository;
	private final ImageRepositoryCustom imageRepositoryCustom;
	private final ImageProductRepository imageProductRepository;
	private final ReviewService reviewService;
	private final InventoryService inventoryService;

	private static final BigDecimal NULL_PERCENT = new BigDecimal("0");

	@Value("${digits.after.decimal}")
	private int digitsAfterDecimal;
	@Value("${image.product.path.base}")
	private String imagePathBase;

	public List<ProductDto> buildProductDtoList(List<Product> products, boolean isMain) {
		List<String> productsSkuCodes = extractSkuCodes(products);

		Map<String, QuantityStatusDto> quantityStatusMap = inventoryService
				.getQuantityStatusBySkuCodeList(productsSkuCodes);

		List<ProductColorDto> productColorList = buildProductColorList(quantityStatusMap);
		Map<String, List<ImageProduct>> imageMap = getImageMapByColor(productColorList);

		if(imageMap.isEmpty()) {
			return new ArrayList<>();
		}
		return products.stream().map(product -> buildProductDto(product, imageMap.get(product.getSkuCode()),
				quantityStatusMap.get(product.getSkuCode()))).toList();
	}

	private List<ProductColorDto> buildProductColorList(Map<String, QuantityStatusDto> quantityStatusMap) {
		List<ProductColorDto> result = new ArrayList<>();
		for (Map.Entry<String, QuantityStatusDto> pair : quantityStatusMap.entrySet()) {
			ProductColorDto dto = new ProductColorDto();
			dto.setProductSkuCode(pair.getKey());
			if (!pair.getValue().getStatus().equals("Немає в наявності")) {
				for (Map.Entry<String, String> colorStatusPair : pair.getValue().getColorQuantityStatus().entrySet()) {
					if (!colorStatusPair.getValue().equals("Немає в наявності")) {
						dto.setColorHex(colorStatusPair.getKey());
						break;
					}
				}
			} else {
				for (Map.Entry<String, String> colorStatusPair : pair.getValue().getColorQuantityStatus().entrySet()) {
					dto.setColorHex(colorStatusPair.getKey());
					break;
				}
			}
			result.add(dto);
		}
		log.info("[ON buildProductColorList] :: build product-color list {}", result);
		return result;
	}

	public List<ProductDto> buildFilteredProductDtoList(List<Product> products, List<String> colors) {
		List<String> productsSkuCodes = extractSkuCodes(products);
		Map<String, QuantityStatusDto> quantityStatusMap = inventoryService
				.getQuantityStatusBySkuCodeList(productsSkuCodes);
		List<ProductColorDto> productColorDtos;
		if (colors.isEmpty()) {
			productColorDtos = buildProductColorList(quantityStatusMap);
		} else {
			productColorDtos = products.stream().flatMap(
					product -> colors.stream().map(colorId -> new ProductColorDto(product.getSkuCode(), colorId)))
					.toList();
		}
		Map<String, List<ImageProduct>> imageMap = getImageMapByColor(productColorDtos);
		
		if(imageMap.isEmpty()) {
			return new ArrayList<>();
		}
		
		return products.stream().map(product -> buildProductDto(product,
				List.of(imageMap.get(product.getSkuCode()).get(0)), quantityStatusMap.get(product.getSkuCode())))
				.toList();
	}

	public ProductDto buildProductDto(Product product, List<ImageProduct> images, QuantityStatusDto quantityStatus) {
		ProductDto productDto = ProductDto.builder()
				.skuCode(product.getSkuCode())
				.name(product.getName())
				.shortDescription(product.getShortDescription())
				.price(roundBigDecimalToZeroDecimalPlace(product.getPrice()))
				.imageDtoList(imageBuilder.buildImageDtoList(images)).build();
		if (quantityStatus.getStatus() != null) {
			productDto.setProductQuantityStatus(quantityStatus.getStatus());
		}
		if (quantityStatus.getColorQuantityStatus() != null) {
			productDto.setColorDtoList(convertMapToColorDtoList(quantityStatus.getColorQuantityStatus()));
		}
		BigDecimal discount = BigDecimal.valueOf(product.getDiscount());
		if (!discount.equals(NULL_PERCENT)) {
			productDto.setDiscount(product.getDiscount());
			productDto.setPriceWithDiscount(roundBigDecimalToZeroDecimalPlace(product.getPriceWithDiscount()));
		}
		log.info("[ON buildProductDto] :: build product dto with skuCode {}", productDto.getSkuCode());
		return productDto;
	}

	public ProductCardDto buildProductCardDto(Product product, String colorId) {
		final String productSkuCode = product.getSkuCode();
		ObjectId categoryId = product.getSubCategory().getParentId();

		String categoryName = categoryRepository.findById(categoryId)
				.map(Category::getName)
				.orElseThrow(() -> new DataNotFoundException(String.format("Category with id = %s doesn't found", categoryId)));

		ProductCardDto productCardDto = ProductCardDto.builder().parentCategoryId(categoryId.toString())
				.categoryName(categoryName)
				.subCategoryName(product.getSubCategory().getName())
				.skuCode(productSkuCode)
				.name(product.getName())
				.description(product.getDescription())
				.price(roundBigDecimalToZeroDecimalPlace(product.getPrice()))
				.materials(product.getMaterials().stream().map(Material::getName).toList())
				.collection(modelMapper.map(product.getCollection(), CollectionDto.class))
				.weight(roundFloatToOneDecimalPlace(product.getWeight()))
				.height(roundFloatToOneDecimalPlace(product.getHeight()))
				.width(roundFloatToOneDecimalPlace(product.getWidth()))
				.depth(roundFloatToOneDecimalPlace(product.getDepth())).maxLoad(product.getMaxLoad()).build();

		BigDecimal discount = BigDecimal.valueOf(product.getDiscount());
		if (!discount.equals(NULL_PERCENT)) {
			productCardDto.setDiscount(product.getDiscount());
			productCardDto.setPriceWithDiscount(roundBigDecimalToZeroDecimalPlace(product.getPriceWithDiscount()));
		}

		buildAdditionalCharacteristics(product, productCardDto);
		buildProductCardDetails(productSkuCode, productCardDto, colorId);
		log.info("[ON buildProductCardDto] :: build product dto with skuCode {}", product.getSkuCode());
		return productCardDto;
	}

	private void buildProductCardDetails(String productSkuCode, ProductCardDto productCardDto, String colorId) {
		List<ImageProduct> images = imageProductRepository.findByProductSkuCodeAndColorId(productSkuCode, colorId);
		if (!images.isEmpty()) {
			productCardDto.setImages(imageBuilder.buildProductCardImageDtos(images));
		}

		QuantityStatusDto colorsQuantityStatus = inventoryService.getProductCardColorQuantityStatus(productSkuCode);
		if (colorsQuantityStatus.getStatus() != null) {
			productCardDto.setQuantityStatus(colorsQuantityStatus.getStatus());
		}

		if (!colorsQuantityStatus.getColorQuantityStatus().isEmpty()) {
			productCardDto.setColors(convertMapToColorDtoList(colorsQuantityStatus.getColorQuantityStatus()));
		}

		List<ReviewResponse> reviews = reviewService.getReviewsForProduct(productSkuCode);
		if (!reviews.isEmpty()) {
			productCardDto.setReviews(reviews);
			productCardDto.setCountOfReviews((byte) reviews.size());
			productCardDto.setAverageRating(roundFloatToOneDecimalPlace(countAverageRating(reviews)));
		}
	}

	private List<ColorQuantityStatusDto> convertMapToColorDtoList(Map<String, String> map) {
		return map.entrySet().stream().map(
				entry -> new ColorQuantityStatusDto(entry.getKey(),
				ColorsEnum.getColorNameByHex(entry.getKey()), entry.getValue())).toList();
	}

	private void buildAdditionalCharacteristics(Product product, ProductCardDto productCardDto) {
		if (product.getNumberOfDoors() != null) {
			productCardDto.setNumberOfDoors(product.getNumberOfDoors());
		}
		if (product.getNumberOfDrawers() != null) {
			productCardDto.setNumberOfDrawers(product.getNumberOfDrawers());
		}
		if (product.getBedLength() != null) {
			productCardDto.setBedLength(product.getBedLength());
		}
		if (product.getBedWidth() != null) {
			productCardDto.setBedWidth(product.getBedWidth());
		}
		if (product.getTransformation() != null) {
			productCardDto.setTransformation(product.getTransformation());
		}
		if (product.getHeightRegulation() != null) {
			productCardDto.setHeightAdjustment(product.getHeightRegulation());
		}
	}

	public List<ProductForBasketDto> buildProductsShopCard(Map<String, Product> productsMap,
			Map<ProductColorDto, ImageProduct> imagesMap, List<ProductColorDto> productColorDtos,
			Map<ProductColorDto, AvailabilityStatusDto> productAvailabilityMap) {
		List<ProductForBasketDto> productShopCards = new ArrayList<>();
		productColorDtos.forEach(productColor -> {
			BigDecimal discount = BigDecimal.valueOf(productsMap.get(productColor.getProductSkuCode()).getDiscount());
			ProductForBasketDto productShopCard = ProductForBasketDto.builder()
					.skuCode(productColor.getProductSkuCode())
					.name(productsMap.get(productColor.getProductSkuCode()).getName())
					.price(productsMap.get(productColor.getProductSkuCode()).getPrice())
					.colorName(ColorsEnum.getColorNameByHex(productColor.getColorHex()))
					.colorHex(productColor.getColorHex()).build();

			if (productAvailabilityMap.get(productColor) != null) {
				productShopCard.setAvailableProductQuantity(
						productAvailabilityMap.get(productColor).getAvailableProductQuantity());
				productShopCard.setQuantityStatus(productAvailabilityMap.get(productColor).getQuantityStatus());
			}

			if (!discount.equals(NULL_PERCENT)) {
				productShopCard.setPriceWithDiscount(roundBigDecimalToZeroDecimalPlace(
						productsMap.get(productColor.getProductSkuCode()).getPriceWithDiscount()));
			}

			if (imagesMap.get(productColor) != null) {
				productShopCard.setImagePath(imagePathBase + imagesMap.get(productColor).getSliderImageName());
			}

			productShopCards.add(productShopCard);
		});

		return productShopCards;
	}

	public SearchResultDto buildSearchResult(List<Product> products) {
		final byte searchResultSize = 4;
		byte indexTo = products.size() < searchResultSize ? (byte) products.size() : searchResultSize;

		List<String> skuCodes = extractSkuCodes(products.subList(0, indexTo));
		Map<String, ImageProduct> imagesMap = imageRepositoryCustom.findMainFirstImagesBySkuCodeIn(skuCodes);
		List<ProductSearchDto> productSearchList = products.stream()
				.limit(searchResultSize)
				.map(product -> mapToProductSearchDto(product, imagesMap.get(product.getSkuCode()))).toList();

		Map<Category, Long> categoriesMap = groupProductsByCategory(products);
		List<CategorySearchDto> categorySearchList = categoriesMap.entrySet().stream()
				.map(entry -> mapToCategorySearchDto(entry.getKey(), entry.getValue().shortValue())).toList();

		return new SearchResultDto(productSearchList, categorySearchList);
	}

	private ProductSearchDto mapToProductSearchDto(Product product, ImageProduct image) {
		ProductSearchDto result = ProductSearchDto.builder().skuCode(product.getSkuCode()).name(product.getName())
				.price(product.getPrice()).priceWithDiscount(product.getPriceWithDiscount())
				.imagePath(imagePathBase + image.getSliderImageName()).colorHex(image.getColor().getId()).build();
		return result;
	}

	private CategorySearchDto mapToCategorySearchDto(Category category, short size) {
		return CategorySearchDto.builder().id(category.getId().toString()).name(category.getName())
				.countOfProducts(size).build();
	}

	private Map<Category, Long> groupProductsByCategory(List<Product> products) {
		return products.stream()
				.collect(Collectors.groupingBy(
						product -> categoryRepository.findById(product.getSubCategory().getParentId())
								.orElseThrow(() -> new DataNotFoundException("No category found")),
						Collectors.counting()));
	}

	private float countAverageRating(List<ReviewResponse> reviews) {
		return (float) reviews.stream().mapToInt(ReviewResponse::getRating).average().getAsDouble();
	}

	private float roundFloatToOneDecimalPlace(Float floatValue) {
		float roundedFloat = 0.0f;
		String format = "%.0f";
		if (floatValue != null) {
			roundedFloat = Float.valueOf(String.format(format, floatValue));
		}
		return roundedFloat;
	}

	private BigDecimal roundBigDecimalToZeroDecimalPlace(BigDecimal value) {
		return value.setScale(digitsAfterDecimal, RoundingMode.HALF_UP);
	}

	private List<String> extractSkuCodes(List<Product> products) {
		return products.stream().map(Product::getSkuCode).toList();
	}

	private Map<String, List<ImageProduct>> getImageMap(List<String> productsSkuCodes, boolean isMain) {
		List<ImageProduct> images = imageRepositoryCustom.findImagesByMainPhotoAndProductSkuCodeIn(productsSkuCodes,
				isMain);
		return images.stream()
				.collect(Collectors.groupingBy(image -> image.getProduct().getSkuCode(), Collectors.toList()));
	}

	private Map<String, List<ImageProduct>> getImageMapByColor(List<ProductColorDto> productColorDtos) {
		if(productColorDtos.isEmpty()) {
			return new HashMap<>();
		}
		return imageRepositoryCustom.findMainImagesByProductColorList(productColorDtos)
				.entrySet().stream()
				.collect(Collectors.groupingBy(pair -> pair.getKey().getProductSkuCode(),
						Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
	}
}
