package com.cozyhome.onlineshop.userservice.security.service.impl;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cozyhome.onlineshop.dto.CategoryDto;
import com.cozyhome.onlineshop.dto.FavoriteProductsDto;
import com.cozyhome.onlineshop.dto.ProductDto;
import com.cozyhome.onlineshop.dto.productcard.ProductCardDto;
import com.cozyhome.onlineshop.dto.request.PageableDto;
import com.cozyhome.onlineshop.productservice.model.Product;
import com.cozyhome.onlineshop.productservice.repository.CategoryRepository;
import com.cozyhome.onlineshop.productservice.repository.ProductRepository;
import com.cozyhome.onlineshop.productservice.repository.ProductRepositoryCustom;
import com.cozyhome.onlineshop.productservice.service.builder.ProductBuilder;
import com.cozyhome.onlineshop.productservice.wrapper.IdWrapper;
import com.cozyhome.onlineshop.userservice.model.FavoriteProduct;
import com.cozyhome.onlineshop.userservice.repository.FavoriteProductRepository;
import com.cozyhome.onlineshop.userservice.security.service.FavoriteProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class FavoriteProductServiceImpl implements FavoriteProductService {

	private final FavoriteProductRepository favoriteProductsRepository;
	private final ProductRepository productRepository;
	private final ProductRepositoryCustom productRepositoryCustom;
	private final ProductBuilder productBuilder;
	private final CategoryRepository categoryRepository;

	@Override
	public void updateUserFavoriteProducts(String userId, String productSkuCode) {
		Optional<FavoriteProduct> favoriteProduct = favoriteProductsRepository
				.findByProductSkuCodeAndUserId(productSkuCode, userId);

		if (favoriteProduct.isPresent()) {
			favoriteProductsRepository.delete(favoriteProduct.get());
			log.info(
					"[ON updateUserFavoriteProducts] :: Item with skuCode [{}] and color hex [{}] was deleted from user's [{}] favorite items.",
					productSkuCode, userId);
		} else {
			favoriteProductsRepository.save(new FavoriteProduct(productSkuCode, userId));
			log.info(
					"[ON updateUserFavoriteProducts] :: Item with skuCode [{}] and color hex [{}] was added to user's [{}] favorite items.",
					productSkuCode, userId);
		}
	}

	@Override
	public List<CategoryDto> getFavoriteCategoriesByUserId(String userId) {
		List<FavoriteProduct> favoriteProducts = favoriteProductsRepository.findAllByUserId(userId);
		List<Product> products = getProductsByFavorites(favoriteProducts);
		List<ObjectId> categoryIds = products.stream().map(product -> product.getSubCategory().getParentId()).toList();
		List<CategoryDto> categories = categoryRepository.findAllByActiveAndIdIn(true, categoryIds).stream()
				.map(category -> new CategoryDto(category.getId().toString(), category.getName())).toList();
		return categories;
	}

	@Override
	public FavoriteProductsDto getFavoriteProductsByUserId(String userId, PageableDto pageable) {
		Page<FavoriteProduct> favoriteProducts = favoriteProductsRepository.findAllByUserId(userId,
				buildPageable(pageable));
		List<Product> products = getProductsByFavorites(favoriteProducts.getContent());
		List<ProductDto> productDtoList = productBuilder.buildProductDtoList(products, true);
		productDtoList.parallelStream().forEach(productDto -> productDto.setFavorite(true));
		FavoriteProductsDto result = FavoriteProductsDto.builder().products(productDtoList)
				.countOfPages((short) favoriteProducts.getTotalPages())
				.countOfProducts((short) favoriteProducts.getTotalElements()).build();

		return result;
	}

	@Override
	public FavoriteProductsDto getFavoriteProductsByUserIdAndCategoryId(String userId, String categoryId,
			PageableDto pageable) {
		List<FavoriteProduct> favoriteProducts = favoriteProductsRepository.findAllByUserId(userId);
		List<String> skuCodesList = favoriteProducts.stream()
				.map(FavoriteProduct::getProductSkuCode).toList();
		List<ObjectId> subCategoriesList = categoryRepository
				.findAllIdsOnlyByActiveAndParentId(true, new ObjectId(categoryId)).stream().map(IdWrapper::getId)
				.toList();

		List<Product> products = productRepositoryCustom.getBySkuCodeInAndCategoryIdsIn(skuCodesList, subCategoriesList,
				buildPageable(pageable));
		if(products.isEmpty()) {
			return new FavoriteProductsDto();
		}
		List<ProductDto> productDtoList = productBuilder.buildProductDtoList(products, true);
		productDtoList.parallelStream().forEach(productDto -> productDto.setFavorite(true));
		FavoriteProductsDto result = FavoriteProductsDto.builder().products(productDtoList)				
				.countOfProducts((short)products.size()).build();
		if(pageable.getSize() > products.size()) {
			result.setCountOfPages((short)1);
		} else {
			result.setCountOfPages((short) (products.size()/pageable.getSize()));
		}
		return result;
	}

	private Pageable buildPageable(PageableDto pageable) {
		return PageRequest.of(pageable.getPage(), pageable.getSize());
	}

	private List<Product> getProductsByFavorites(List<FavoriteProduct> favoriteProducts) {
		List<String> productSkuCodes = favoriteProducts.stream().map(product -> product.getProductSkuCode()).toList();
		List<Product> products = productRepository.findAllByStatusNotDeletedAndSkuCodeIn(productSkuCodes);
		return products;
	}

	@Override
	public void markFavoritesForUser(String userId, List<ProductDto> products) {
		List<String> favoriteSkuCodes = favoriteProductsRepository.findAllByUserId(userId).stream().map(FavoriteProduct::getProductSkuCode).toList();		
		products.parallelStream().forEach(productDto -> productDto.setFavorite(favoriteSkuCodes.contains(productDto.getSkuCode())));
	}

	@Override
	public void markFavoritesForUser(String userId, ProductCardDto productCard) {
		productCard.setFavorite(
				favoriteProductsRepository.existsByProductSkuCodeAndUserId(productCard.getSkuCode(), userId));
	}
}
