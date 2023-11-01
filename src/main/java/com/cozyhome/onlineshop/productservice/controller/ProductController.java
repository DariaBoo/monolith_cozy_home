package com.cozyhome.onlineshop.productservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cozyhome.onlineshop.dto.ProductDto;
import com.cozyhome.onlineshop.dto.ProductForBasketDto;
import com.cozyhome.onlineshop.dto.ProductStatusDto;
import com.cozyhome.onlineshop.dto.filter.FilterDto;
import com.cozyhome.onlineshop.dto.productcard.ProductCardDto;
import com.cozyhome.onlineshop.dto.request.PageableDto;
import com.cozyhome.onlineshop.dto.request.ProductColorDto;
import com.cozyhome.onlineshop.dto.request.SortDto;
import com.cozyhome.onlineshop.productservice.controller.swagger.SwaggerResponse;
import com.cozyhome.onlineshop.productservice.service.ProductService;
import com.cozyhome.onlineshop.validation.ValidId;
import com.cozyhome.onlineshop.validation.ValidSkuCode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin({ "${api.front.base_url}", "${api.front.localhost}", "${api.front.test_url}",
        "${api.front.additional_url}", "${api.front.main.url}" })
@Tag(name = "Product")
@ApiResponse
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("${api.basePath}/product")
public class ProductController {

	private final ProductService productService;
	
	@Value("${header.name.user-id}")
    private String userIdAttribute;

    @Operation(summary = "Fetch all statuses of product", description = "Retrieve all statuses of products.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
    @GetMapping("/statuses")
	public ResponseEntity<List<ProductStatusDto>> getProductsStatuses() {
		return new ResponseEntity<>(productService.getProductStatuses(), HttpStatus.OK);
	}

    @Operation(summary = "Fetch random products by status", description = "Fetch a specified number of random products based on the provided status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION),
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_400, description = SwaggerResponse.Message.CODE_400) })
    @GetMapping("/homepage/status")
    public ResponseEntity<List<ProductDto>> getRandomProductsByStatus(@RequestParam Byte status,
                                                                      @RequestParam int countOfProducts, HttpServletRequest request) {
    	String userId = (String) request.getAttribute(userIdAttribute);
    	List<ProductDto> products = productService.getRandomProductsByStatus(status, countOfProducts);
    	if(userId != null) {
    		productService.markFavoriteForUser(userId, products);
    	}
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get Random Products by Status and Category ID", description = "Fetch a specified number of random products based on the provided status and category ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION),
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_400, description = SwaggerResponse.Message.CODE_400) })
    @GetMapping("/homepage/category-status")
    public ResponseEntity<List<ProductDto>> getRandomProductsByStatusAndCategoryId(@RequestParam Byte status,
                                                                                   @RequestParam @ValidId String categoryId,
                                                                                   @RequestParam int countOfProducts, HttpServletRequest request) {
    	String userId = (String) request.getAttribute(userIdAttribute);
    	List<ProductDto> products = productService.getRandomProductsByStatusAndCategoryId(status, categoryId, countOfProducts);
    	if(userId != null) {
    		productService.markFavoriteForUser(userId, products);
    	}
    	return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get Products by Category ID", description = "Fetch products based on the provided category ID. Optionally, you can also provide a sub-category ID to filter the results.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION),
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_400, description = SwaggerResponse.Message.CODE_400) })
    @GetMapping("/catalog/category")
    public ResponseEntity<List<ProductDto>> getProductsByCategoryId(@RequestParam @ValidId String categoryId,
                                                                    @Valid PageableDto pageable, HttpServletRequest request) {
    	String userId = (String) request.getAttribute(userIdAttribute);
    	List<ProductDto> products = productService.getProductsByCategoryId(categoryId, pageable);
    	if(userId != null) {
    		productService.markFavoriteForUser(userId, products);
    	}
    	return ResponseEntity.ok(products);
    }

    @Operation(summary = "Filter Products by Criterias", description = "Filter products based on the provided criterias. You can specify filtering options in the request body, along with pagination and sorting preferences.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION),
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_400, description = SwaggerResponse.Message.CODE_400) })
    @PostMapping("/filter")
    public ResponseEntity<List<ProductDto>> getFilteredProducts(@RequestBody FilterDto filter,
                                                                      @Valid PageableDto pageable,
                                                                      @Valid SortDto sortDto) {
        return new ResponseEntity<>(productService.getFilteredProducts(filter, pageable, sortDto), HttpStatus.OK);
    }

    @Operation(summary = "Get Filter Parameters", description = "Get the filter parameters for filtering products.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION),
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_400, description = SwaggerResponse.Message.CODE_400) })
    @PostMapping("/filter/parameters")
    public ResponseEntity<FilterDto> getFilterParameters(@RequestBody @Valid FilterDto filter, @RequestParam byte size) {
        return new ResponseEntity<>(productService.getFilterParameters(filter, size), HttpStatus.OK);
    }

    @Operation(summary = "Get Product by skuCode and color's hex", description = "Get product by provided product's skuCode and color's hex.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION),
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_400, description = SwaggerResponse.Message.CODE_400) })
    @PostMapping("/skuCode")
    public ResponseEntity<ProductCardDto> getProductCard(@RequestBody @Valid ProductColorDto dto, HttpServletRequest request){
    	String userId = (String) request.getAttribute(userIdAttribute);
    	ProductCardDto productCardDto = productService.getProductCard(dto);
    	if(userId != null) {
    		productService.markFavoriteForUser(userId, productCardDto);
    	}
    	return ResponseEntity.ok().body(productCardDto);
    }

    @Operation(summary = "Get Products by collection name excluding provided skuCode", description = "Get list of products by collection name excluding provided skuCode.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION),
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_400, description = SwaggerResponse.Message.CODE_400) })
    @GetMapping("/collection")
    public ResponseEntity<List<ProductDto>> getProductsByCollection(@RequestParam String collectionId,
                                                                    @RequestParam @ValidSkuCode String productSkuCode, HttpServletRequest request){
    	String userId = (String) request.getAttribute(userIdAttribute);
    	List<ProductDto> products = productService.getProductsByCollectionExcludeSkuCode(collectionId, productSkuCode);
    	if(userId != null) {
    		productService.markFavoriteForUser(userId, products);
    	}
    	return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get products information for basket by product sku code and color hex", description = "Get list of products information for basket by product sku code and color hex.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION),
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_400, description = SwaggerResponse.Message.CODE_400) })
    @PostMapping("/basket")
    public ResponseEntity<List<ProductForBasketDto>> getProductsForBasket(@RequestBody @Valid List<ProductColorDto> productColorDtos){
        return ResponseEntity.ok().body(productService.getProductsForBasket(productColorDtos));
    }
}
