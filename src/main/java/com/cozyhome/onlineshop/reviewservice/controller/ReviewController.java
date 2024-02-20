package com.cozyhome.onlineshop.reviewservice.controller;

import java.util.List;

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

import com.cozyhome.onlineshop.dto.review.ReviewRequest;
import com.cozyhome.onlineshop.dto.review.ReviewResponse;
import com.cozyhome.onlineshop.productservice.controller.swagger.CommonApiResponses;
import com.cozyhome.onlineshop.productservice.controller.swagger.SwaggerResponse;
import com.cozyhome.onlineshop.reviewservice.service.ReviewService;
import com.cozyhome.onlineshop.validation.ValidSkuCode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin({ "${api.front.base_url}", "${api.front.localhost}", "${api.front.test_url}",
	"${api.front.additional_url}", "${api.front.main.url}", "${api.front.temporal.url}" })

@Validated
@CommonApiResponses
@Tag(name = "Review")
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.basePath}/review")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "Fetch review for product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
    @GetMapping("/product")
    public ResponseEntity<List<ReviewResponse>> getReviewsForProduct(@RequestParam @ValidSkuCode String productSkuCode) {
        return new ResponseEntity<>(reviewService.getReviewsForProduct(productSkuCode), HttpStatus.OK);
    }

    @Operation(summary = "Add new review.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_201, description = SwaggerResponse.Message.CODE_201_CREATED_DESCRIPTION) })
    @PostMapping("/new")
    public ResponseEntity<ReviewResponse> addNewReview(@RequestBody @Valid ReviewRequest review) {
        return new ResponseEntity<>(reviewService.addNewReview(review), HttpStatus.CREATED);
    }
 }
