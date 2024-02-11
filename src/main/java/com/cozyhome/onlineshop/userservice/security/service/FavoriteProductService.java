package com.cozyhome.onlineshop.userservice.security.service;

import java.util.List;

import com.cozyhome.onlineshop.dto.CategoryDto;
import com.cozyhome.onlineshop.dto.FavoriteProductsDto;
import com.cozyhome.onlineshop.dto.ProductDto;
import com.cozyhome.onlineshop.dto.productcard.ProductCardDto;
import com.cozyhome.onlineshop.dto.request.PageableDto;

public interface FavoriteProductService {

	void updateUserFavoriteProducts(String userId, String productSkuCode);
	
	FavoriteProductsDto getFavoriteProductsByUserId(String userId, PageableDto pageable);
	
	List<CategoryDto> getFavoriteCategoriesByUserId(String userId);
	
	FavoriteProductsDto getFavoriteProductsByUserIdAndCategoryId(String userId, String categoryId,
			PageableDto pageable);
	
    void markFavoritesForUser(String userId, List<ProductDto> products);
    
    void markFavoritesForUser(String userId, ProductCardDto productCard);
}
