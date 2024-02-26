package com.cozyhome.onlineshop.productservice.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.cozyhome.onlineshop.dto.ProductDto;
import com.cozyhome.onlineshop.dto.ProductForBasketDto;
import com.cozyhome.onlineshop.dto.ProductStatusDto;
import com.cozyhome.onlineshop.dto.SearchResultDto;
import com.cozyhome.onlineshop.dto.filter.FilterDto;
import com.cozyhome.onlineshop.dto.inventory.ProductAvailabilityDto;
import com.cozyhome.onlineshop.dto.inventory.AvailabilityStatusDto;
import com.cozyhome.onlineshop.dto.productcard.ProductCardDto;
import com.cozyhome.onlineshop.dto.request.PageableDto;
import com.cozyhome.onlineshop.dto.request.ProductColorDto;
import com.cozyhome.onlineshop.dto.request.SortDto;
import com.cozyhome.onlineshop.exception.DataNotFoundException;
import com.cozyhome.onlineshop.inventoryservice.service.InventoryService;
import com.cozyhome.onlineshop.productservice.model.ImageProduct;
import com.cozyhome.onlineshop.productservice.model.Product;
import com.cozyhome.onlineshop.productservice.model.enums.ProductStatus;
import com.cozyhome.onlineshop.productservice.repository.CategoryRepository;
import com.cozyhome.onlineshop.productservice.repository.ImageRepositoryCustom;
import com.cozyhome.onlineshop.productservice.repository.ProductRepository;
import com.cozyhome.onlineshop.productservice.repository.ProductRepositoryCustom;
import com.cozyhome.onlineshop.productservice.service.CategoryService;
import com.cozyhome.onlineshop.productservice.service.ProductService;
import com.cozyhome.onlineshop.productservice.service.builder.ProductBuilder;
import com.cozyhome.onlineshop.productservice.service.builder.ProductFilterParametersBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepositoryCustom productRepositoryCustom;
	private final ImageRepositoryCustom imageRepositoryCustom;
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final CategoryService categoryService;
	private final ProductBuilder productBuilder;
	private final ProductFilterParametersBuilder productFilterParametersBuilder;
	private final InventoryService inventoryService;

	private final boolean isMain = true;

	private static final String DIRECTION_ASC = "asc";
	private static final Direction DEFAULT_DIRECTION = Direction.DESC;
	private static final String DEFAULT_SORTING = "available";
	private static final String ADDITIONAL_SORTING = "_id";
	private static final String SORTING_BY_PRICE = "price";
	private static final String SORTING_BY_PRICE_WITH_DISCOUNT = "priceWithDiscount";

	@Override
	public List<ProductStatusDto> getProductStatuses() {
		return Arrays.stream(ProductStatus.values()).filter(status -> status != ProductStatus.DELETED)
				.map(status -> new ProductStatusDto(status.toString(), status.getDescription())).toList();
	}

//	@Cacheable(value = "randomProduct", key = "#status", cacheManager = "cacheManagerWithExpiration")
	@Override
	public List<ProductDto> getRandomProductsByStatus(Byte status, int productCount) {
		List<Product> products = productRepositoryCustom
				.getRandomByStatusAndInStock(ProductStatus.valueOfDescription(status), productCount);
		if(products.isEmpty()) {
			return new ArrayList<>();
		}
		return productBuilder.buildProductDtoList(products, isMain);
	}

//	@Cacheable(value = "randomProductsByStatusAndCategoryId", key = "#categoryId", cacheManager = "cacheManagerWithExpiration")
	@Override
	public List<ProductDto> getRandomProductsByStatusAndCategoryId(Byte status, String categoryId,
			int countOfProducts) {
		List<ObjectId> categoriesIds = categoryService.getCategoriesIdsByParentId(categoryId);
		if (categoriesIds.isEmpty()) {
			log.error(
					"[ON getRandomProductsByStatusAndCategoryId]:: Subcategory for category with id {} doesn't exist.",
					categoryId);
			throw new DataNotFoundException(
					String.format("Subcategory for category with id %s doesn't exist.", categoryId));
		}
		List<Product> products = productRepositoryCustom.getRandomByStatusAndCategoryIdAndInStock(
				ProductStatus.valueOfDescription(status), categoriesIds, countOfProducts);
		if(products.isEmpty()) {
			return new ArrayList<>();
		}
		return productBuilder.buildProductDtoList(products, isMain);
	}

//	@Cacheable(value = "productsByCategoryId", key = "#categoryId")
	@Override
	public List<ProductDto> getProductsByCategoryId(String categoryId, PageableDto pageable) {
		boolean isSubcategory = categoryRepository.hasParentById(new ObjectId(categoryId));
		Pageable page = buildPageable(pageable, new SortDto());
		Page<Product> products;
		if (isSubcategory) {
			products = productRepository.findAllByStatusNotDeletedAndCategoryId(new ObjectId(categoryId), page);
		} else {
			List<ObjectId> objectIds = categoryService.getCategoriesIdsByParentId(categoryId);
			products = productRepository.findAllByStatusNotDeletedAndCategoryIdIn(objectIds, page);
		}
		List<ProductDto> result = productBuilder.buildProductDtoList(products.getContent(), isMain);
		log.info("[ON getProductsByCategoryId] :: recieve products [count:{}] for category id {}, page {}, size {}",
				result.size(), categoryId, pageable.getPage(), pageable.getSize());
		return result;
	}

	@Override
	public List<ProductDto> getFilteredProducts(FilterDto filter, PageableDto pageable, SortDto sortDto) {
		Pageable currentPageable = buildPageable(pageable, sortDto);
		List<Product> products = productRepositoryCustom.filterProductsByCriterias(filter, currentPageable);
		List<String> colors;
		if (filter.getColors() == null) {
			colors = new ArrayList<>();
		} else {
			colors = filter.getColors().stream().map(color -> color.getId()).toList();
		}
		return productBuilder.buildFilteredProductDtoList(products, colors);
	}

	@Override
	public FilterDto getFilterParameters(FilterDto filter, byte size) {
		List<Product> filteredProducts = productRepositoryCustom.filterProductsByCriterias(filter, null);
		if (filteredProducts.isEmpty()) {
			log.info("[ON getFilterParameters]:: Products with the given parameters don't found.");
			return new FilterDto();
		}
		List<ObjectId> objectIds = categoryService.getCategoriesIdsByParentId(filter.getParentCategoryId());
		if (objectIds.isEmpty()) {
			log.info("[ON getFilterParameters]:: Category with id {} hasn't subcategories.",
					filter.getParentCategoryId());
			return productFilterParametersBuilder.buildFilterParameters(filteredProducts, filteredProducts, size);
		}
		List<Product> notFilteredProducts = productRepository.findAllByStatusNotDeletedAndCategoryIdIn(objectIds);
		if (notFilteredProducts.isEmpty()) {
			log.info("[ON getFilterParameters]:: Products for category with id {} don't found.",
					filter.getParentCategoryId());
			return productFilterParametersBuilder.buildFilterParameters(filteredProducts, filteredProducts, size);
		}
		return productFilterParametersBuilder.buildFilterParameters(filteredProducts, notFilteredProducts, size);
	}

	@Override
	public ProductCardDto getProductCard(ProductColorDto productColor) {
		Optional<Product> product = productRepository.findBySkuCode(productColor.getProductSkuCode());
		if (!product.isPresent()) {
			log.info("[ON getProductCard]:: Product with sku code {} and color hex {} doesn't exist.",
					productColor.getProductSkuCode(), productColor.getColorHex());
			return new ProductCardDto();
		}
		return productBuilder.buildProductCardDto(product.get(), productColor.getColorHex());
	}

	@Override
	public List<ProductDto> getProductsByCollectionExcludeSkuCode(String collectionId,
			@Param("#skuCode") String skuCodeToExclude) {
		List<Product> products = productRepository
				.findAllByStatusNotDeletedAndCollectionIdExcludeSkuCode(new ObjectId(collectionId), skuCodeToExclude);
		return productBuilder.buildProductDtoList(products, isMain);
	}

	@Override
	public List<ProductForBasketDto> getProductsForBasket(List<ProductColorDto> productColorDtos) {
		Map<String, Product> productMap = new HashMap<>();
		Map<ProductColorDto, AvailabilityStatusDto> productAvailableAndStatusMap = new HashMap<>();
		List<String> skuCodes = productColorDtos.stream().map(ProductColorDto::getProductSkuCode).toList();

		List<Product> products = productRepository.findAllByStatusNotDeletedAndSkuCodeIn(skuCodes);
		if (products.isEmpty()) {
			log.info("[ON getProductsForBasket]:: Products for basket don't found.");
			return new ArrayList<>();
		}
		Map<ProductColorDto, ImageProduct> imagesMap = imageRepositoryCustom
				.findMainImagesByProductColorList(productColorDtos);
		List<ProductAvailabilityDto> productAvailabilityDto = inventoryService.getProductAvailableStatus(productColorDtos);

		products.forEach(product -> productMap.put(product.getSkuCode(), product));
		if (!productAvailabilityDto.isEmpty()) {
			productAvailabilityDto.forEach(dto -> productAvailableAndStatusMap.put(dto.getProductColorDto(),
					dto.getAvailabilityStatusDto()));
		}

		return productBuilder.buildProductsShopCard(productMap, imagesMap, productColorDtos,
				productAvailableAndStatusMap);
	}

	@Override
	public SearchResultDto searchProducts(String keyWord) {
		String decodedKeyword = decodeKeyword(keyWord);
		List<Product> products = productRepositoryCustom.search(decodedKeyword);
		return productBuilder.buildSearchResult(products);
	}
	
	private String decodeKeyword(String encodedKeyword) {
        try {            
            return URLDecoder.decode(encodedKeyword, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return encodedKeyword;
        }
    }


	private Pageable buildPageable(PageableDto pageable, SortDto sortDto) {
		List<Order> orders = new ArrayList<>();
		orders.add(new Order(DEFAULT_DIRECTION, DEFAULT_SORTING));
						
		if (sortDto.getFieldName() != null && sortDto.getDirection() != null) {
			Direction direction = sortDto.getDirection().equals(DIRECTION_ASC) ? Direction.ASC : Direction.DESC;
			if (sortDto.getFieldName().equalsIgnoreCase(SORTING_BY_PRICE)) {
				orders.add(new Order(direction, SORTING_BY_PRICE_WITH_DISCOUNT));
			}
			orders.add(new Order(direction, sortDto.getFieldName()));
		} else {
			orders.add(new Order(DEFAULT_DIRECTION, ADDITIONAL_SORTING));
		}

		return PageRequest.of(
	            pageable.getPage(),
	            pageable.getSize(),
	            Sort.by(orders.stream().map(order -> new Sort.Order(order.getDirection(), order.getProperty())).collect(Collectors.toList()))
	    );
	}
}
