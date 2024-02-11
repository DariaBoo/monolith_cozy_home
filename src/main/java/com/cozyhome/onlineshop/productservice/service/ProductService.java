package com.cozyhome.onlineshop.productservice.service;

import java.util.List;

import com.cozyhome.onlineshop.dto.ProductDto;
import com.cozyhome.onlineshop.dto.ProductForBasketDto;
import com.cozyhome.onlineshop.dto.SearchResultDto;
import com.cozyhome.onlineshop.dto.ProductStatusDto;
import com.cozyhome.onlineshop.dto.filter.FilterDto;
import com.cozyhome.onlineshop.dto.productcard.ProductCardDto;
import com.cozyhome.onlineshop.dto.request.PageableDto;
import com.cozyhome.onlineshop.dto.request.ProductColorDto;
import com.cozyhome.onlineshop.dto.request.SortDto;

public interface ProductService {
	
    List<ProductStatusDto> getProductStatuses();

    List<ProductDto> getRandomProductsByStatus(Byte status, int productCount);

    List<ProductDto> getProductsByCategoryId(String categoryId, PageableDto pageable);

    List<ProductDto> getRandomProductsByStatusAndCategoryId(Byte status, String categoryId, int countOfProducts);

    List<ProductDto> getFilteredProducts(FilterDto filter, PageableDto pageable, SortDto sortDto);

    FilterDto getFilterParameters(FilterDto filter, byte size);

    ProductCardDto getProductCard(ProductColorDto dto);

    List<ProductDto> getProductsByCollectionExcludeSkuCode(String collection, String skuCodeToExclude);

    List<ProductForBasketDto> getProductsForBasket(List<ProductColorDto> dto);
    
    SearchResultDto searchProducts(String keyWord);
}
