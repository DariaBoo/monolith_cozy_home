package com.cozyhome.onlineshop.basketservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cozyhome.onlineshop.basketservice.service.BasketService;
import com.cozyhome.onlineshop.dto.basket.BasketDto;
import com.cozyhome.onlineshop.dto.basket.BasketItemDto;
import com.cozyhome.onlineshop.productservice.controller.swagger.SwaggerResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = { "${api.front.base_url}", "${api.front.localhost}", "${api.front.test_url}",
		"${api.front.additional_url}", "${api.front.main.url}", "${api.front.temporal.url}", "${api.front.test2.url}"}, allowedHeaders = {
				"Authorization", "Content-Type" }, exposedHeaders = { "Access-Control-Allow-Methods" })
@Tag(name = "Basket")
@ApiResponse
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("${api.secure.basePath}/basket")
public class BasketSecuredController {

	private final BasketService basketService;

	@Value("${header.name.user-id}")
	private String userIdAttribute;

	@Operation(summary = "Get user's basket.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
	@Secured({ "ROLE_CUSTOMER", "ROLE_ADMIN", "ROLE_MANAGER" })
	@GetMapping()
	public ResponseEntity<List<BasketDto>> getBasket(HttpServletRequest request) {
		String userId = (String) request.getAttribute(userIdAttribute);
		return ResponseEntity.ok(basketService.getBasket(userId));
	}

	@Operation(summary = "Refresh user's basket.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
	@Secured({ "ROLE_CUSTOMER", "ROLE_ADMIN", "ROLE_MANAGER" })
	@PostMapping()
	public ResponseEntity<List<BasketDto>> mergeUserBaskets(HttpServletRequest request,
			@Valid @RequestBody List<BasketItemDto> dtoList) {

		String userId = (String) request.getAttribute(userIdAttribute);		
		return ResponseEntity.ok(basketService.mergeUserBaskets(userId, dtoList));
	}
	
	@Operation(summary = "Replace user's basket when logging out.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
	@Secured({ "ROLE_CUSTOMER", "ROLE_ADMIN", "ROLE_MANAGER" })
	@PostMapping("/replace")
	public ResponseEntity<Void> replaceBasket(HttpServletRequest request,
			@Valid @RequestBody List<BasketItemDto> dtoList) {
		String userId = (String) request.getAttribute(userIdAttribute);
		basketService.replaceBasket(userId, dtoList);
		return ResponseEntity.ok().build();
	}
}
