package com.cozyhome.onlineshop.productservice.controller;

import com.cozyhome.onlineshop.dto.CategoryWithIconDto;
import com.cozyhome.onlineshop.dto.CategoryWithPhotoDto;
import com.cozyhome.onlineshop.dto.CategoryWithSubCategoriesDto;
import com.cozyhome.onlineshop.productservice.controller.swagger.CommonApiResponses;
import com.cozyhome.onlineshop.productservice.controller.swagger.SwaggerResponse;
import com.cozyhome.onlineshop.productservice.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin({ "${api.front.base_url}", "${api.front.localhost}", "${api.front.test_url}",
		"${api.front.additional_url}", "${api.front.main.url}", "${api.front.temporal.url}" })
@Tag(name = "Category")
@CommonApiResponses
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.basePath}/category")
public class CategoryController {
	private final CategoryService categoryService;

	@Operation(summary = "Fetch all categories", description = "Fetch all categories with id, name, spriteIcon")
	@ApiResponses(value = {
			@ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
	@GetMapping
	public ResponseEntity<List<CategoryWithIconDto>> getCategoryWithIcon() {
		return new ResponseEntity<>(categoryService.getCategoryWithIcon(), HttpStatus.OK);
	}

	@Operation(summary = "Fetch all categories with subcategories for catalog", description = "Fetch all categories with subcategories and images for categories")
	@ApiResponses(value = {
			@ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
	@GetMapping("/categories")
	public ResponseEntity<List<CategoryWithSubCategoriesDto>> getCategoriesWithSubcategoriesAndPhoto() {
		return new ResponseEntity<>(categoryService.getCategoriesWithPhoto(), HttpStatus.OK);
	}

	@Operation(summary = "Fetch all categories with photos for homepage", description = "Fetch all categories with photos for homepage")
	@ApiResponses(value = {
			@ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
	@GetMapping("/homepage/categories")
	public ResponseEntity<List<CategoryWithPhotoDto>> getCategoriesWithPhoto() {
		return new ResponseEntity<>(categoryService.getCategoriesForHomepage(), HttpStatus.OK);
	}
}
