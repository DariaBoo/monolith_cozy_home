package com.cozyhome.onlineshop.productservice.controller;

import com.cozyhome.onlineshop.exception.DataNotFoundException;
import com.cozyhome.onlineshop.dto.ImageDto;
import com.cozyhome.onlineshop.dto.PopUpImageDto;
import com.cozyhome.onlineshop.dto.productcard.ProductCardImageDto;
import com.cozyhome.onlineshop.dto.request.ProductColorDto;
import com.cozyhome.onlineshop.productservice.controller.swagger.CommonApiResponses;
import com.cozyhome.onlineshop.productservice.controller.swagger.SwaggerResponse;
import com.cozyhome.onlineshop.productservice.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin({ "${api.front.base_url}", "${api.front.localhost}", "${api.front.test_url}",
		"${api.front.additional_url}", "${api.front.main.url}", "${api.front.temporal.url}" })
@Tag(name = "Image")
@CommonApiResponses
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("${api.basePath}/image")
public class ImageController {
	private final ResourceLoader resourceLoader;
	private final ImageService imageService;

	@Value("${imageContentType}")
	private String imageContentType;

	@Operation(summary = "Fetch image", description = "Fetch image by image's url")
	@ApiResponses(value = {
			@ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION),
			@ApiResponse(responseCode = SwaggerResponse.Code.CODE_400, description = SwaggerResponse.Message.CODE_400)})
	@GetMapping
	public ResponseEntity<Resource> getImage(@RequestParam @NotBlank String imageName) {
		Resource resource = resourceLoader.getResource("classpath:images/" + imageName);
		if (!resource.exists()) {
			throw new DataNotFoundException("Image with name: " + imageName + " not found.");
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(imageContentType)).body(resource);
	}

	@Operation(summary = "Fetch product's image by color for preview", description = "Fetch product's image with id, image path and color by product's skuCode and color's hex for preview")
	@ApiResponses(value = {
			@ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION),
			@ApiResponse(responseCode = SwaggerResponse.Code.CODE_400, description = SwaggerResponse.Message.CODE_400)})
	@PostMapping("/product-color")
	public ResponseEntity<ImageDto> getPreviewImageForProductByColor(@RequestBody @Valid ProductColorDto productColor) {
		return ResponseEntity.ok().body(imageService.getPreviewImageForProductByColor(productColor));
	}

	@Operation(summary = "Fetch product's pop_up images by color", description = "Fetch product's pop_up image with id, image path")
	@ApiResponses(value = {
			@ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION),
			@ApiResponse(responseCode = SwaggerResponse.Code.CODE_400, description = SwaggerResponse.Message.CODE_400)})
	@PostMapping ("/popup-image")
    public ResponseEntity<List<PopUpImageDto>> getPopUpImageForProductByColor(@RequestBody @Valid ProductColorDto productColor) {
        return ResponseEntity.ok().body(imageService.getPopUpImageForProductByColor(productColor));
    }

	@Operation(summary = "Fetch product card images by color", description = "Fetch images for product card by product's skuCode and color's hex")
	@ApiResponses(value = {
			@ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION),
			@ApiResponse(responseCode = SwaggerResponse.Code.CODE_400, description = SwaggerResponse.Message.CODE_400)})
	@PostMapping ("/product-card-image")
	public ResponseEntity<List<ProductCardImageDto>> getProductCardImagesByColor(@RequestBody @Valid ProductColorDto productColor){
		return ResponseEntity.ok().body(imageService.getProductCardImagesByColor(productColor));
	}
}
