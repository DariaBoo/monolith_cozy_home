package com.cozyhome.onlineshop.userservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cozyhome.onlineshop.dto.CategoryDto;
import com.cozyhome.onlineshop.dto.FavoriteProductsDto;
import com.cozyhome.onlineshop.dto.request.PageableDto;
import com.cozyhome.onlineshop.dto.user.PasswordUpdateRequest;
import com.cozyhome.onlineshop.dto.user.UserInformationRequest;
import com.cozyhome.onlineshop.dto.user.UserInformationResponse;
import com.cozyhome.onlineshop.productservice.controller.swagger.SwaggerResponse;
import com.cozyhome.onlineshop.userservice.security.jwt.JwtTokenUtil;
import com.cozyhome.onlineshop.userservice.security.service.FavoriteProductService;
import com.cozyhome.onlineshop.userservice.security.service.TokenBlackListService;
import com.cozyhome.onlineshop.userservice.security.service.UserService;
import com.cozyhome.onlineshop.validation.ValidId;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = { "${api.front.base_url}", "${api.front.localhost}", "${api.front.test_url}",
		"${api.front.additional_url}", "${api.front.main.url}", "${api.front.temporal.url}", "${api.front.test2.url}" }, allowedHeaders = { "Authorization", "Content-Type"},
	    exposedHeaders = { "Access-Control-Allow-Methods" })
@Tag(name = "User")
@ApiResponse
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("${api.secure.basePath}/user")
public class UserSecuredController {
    @Value("${header.name.user-id}")
    private String userIdAttribute;
    private final UserService userService;
    private final TokenBlackListService tokenBlackListService;
    private final JwtTokenUtil jwtTokenUtil;
    private final FavoriteProductService favoriteProductService;

    @Operation(summary = "Update user information in personal account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
    @Secured({"ROLE_CUSTOMER", "ROLE_MANAGER", "ROLE_ADMIN"})
    @PutMapping("/profile/update")
    public ResponseEntity<UserInformationResponse> updateUserInfo(@RequestBody @Valid UserInformationRequest userInformationDto,
                                                                  HttpServletRequest request) {
        String userId = (String) request.getAttribute(userIdAttribute);
        return ResponseEntity.ok(userService.updateUserData(userInformationDto, userId));
    }

    @Operation(summary = "Update user password in personal account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
    @Secured({"ROLE_CUSTOMER", "ROLE_MANAGER", "ROLE_ADMIN"})
    @PutMapping("/profile/update/pass")
    public ResponseEntity<Void> updateUserPassword(@RequestBody @Valid PasswordUpdateRequest passwords,
                                                                  HttpServletRequest request) {
        String userId = (String) request.getAttribute(userIdAttribute);
        userService.updateUserPassword(passwords, userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get user information from personal account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
    @Secured({"ROLE_CUSTOMER", "ROLE_MANAGER", "ROLE_ADMIN"})
    @GetMapping("/profile")
    public ResponseEntity<UserInformationResponse> getUserInfo(HttpServletRequest request) {
        String userId = (String) request.getAttribute(userIdAttribute);
        return ResponseEntity.ok(userService.getUserInfo(userId));
    }

    @Operation(summary = "Logout.", description = "Logout.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
    @Secured({"ROLE_CUSTOMER", "ROLE_MANAGER", "ROLE_ADMIN"})
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String jwtToken = jwtTokenUtil.resolveToken(request);
        tokenBlackListService.saveTokenToBlackList(jwtToken);
        log.warn("[ON logout] :: JwtToken {} was added to Black List.", jwtToken);
        return ResponseEntity.ok().build();
    } 
    
    @Operation(summary = "Add product to favorite.", description = "Add product to favorite by product skuCode and color hex.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
    @Secured({"ROLE_CUSTOMER", "ROLE_MANAGER", "ROLE_ADMIN"})
    @PostMapping("/favorites")
    public ResponseEntity<Void> updateUserFavoriteProducts(HttpServletRequest request, @Valid @RequestParam String productSkuCode) {
    	String userId = (String) request.getAttribute(userIdAttribute);        
        favoriteProductService.updateUserFavoriteProducts(userId, productSkuCode);
        return ResponseEntity.ok().build();
    } 
    
    @Operation(summary = "Get all of the user's favorite products.", description = "Get all of the user's favorite products.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
    @Secured({"ROLE_CUSTOMER", "ROLE_MANAGER", "ROLE_ADMIN"})
    @GetMapping("/favorites")
    public ResponseEntity<FavoriteProductsDto> getFavoriteProductsForUserId(HttpServletRequest request, PageableDto pageable) {
    	String userId = (String) request.getAttribute(userIdAttribute);
        log.info("[ON getFavoriteProductsForUser] :: Get all favorite products for user with id {}", userId);
        return ResponseEntity.ok(favoriteProductService.getFavoriteProductsByUserId(userId, pageable));
    } 
    
    @Operation(summary = "Get user's favorite products by category id.", description = "Get user's favorite products by category id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
    @Secured({"ROLE_CUSTOMER", "ROLE_MANAGER", "ROLE_ADMIN"})
    @GetMapping("/favorites/category-id")
    public ResponseEntity<FavoriteProductsDto> getFavoriteProductsForUserAndCategoryId(HttpServletRequest request, @RequestParam @ValidId String categoryId,
    		PageableDto pageable) {
    	String userId = (String) request.getAttribute(userIdAttribute);
        log.info("[ON getFavoriteProductsForUserAndCategoryId] :: Get all favorite items for user with id {} and category id {}.", userId, categoryId);
        return ResponseEntity.ok(favoriteProductService.getFavoriteProductsByUserIdAndCategoryId(userId, categoryId, pageable));
    }
    
    @Operation(summary = "Get categories for favorite products by user id.", description = "Get categories for favorite products by user id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
    @Secured({"ROLE_CUSTOMER", "ROLE_MANAGER", "ROLE_ADMIN"})
    @GetMapping("/favorites/categories")
    public ResponseEntity<List<CategoryDto>> getFavoriteCategoriesByUserId(HttpServletRequest request) {
    	String userId = (String) request.getAttribute(userIdAttribute);
        log.info("[ON getFavoriteProductsForUserAndCategoryId] :: Get all favorite categories for user with id {}.", userId);
        return ResponseEntity.ok(favoriteProductService.getFavoriteCategoriesByUserId(userId));
    }
}
