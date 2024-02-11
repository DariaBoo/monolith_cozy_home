package com.cozyhome.onlineshop.reviewservice.service;

import com.cozyhome.onlineshop.dto.review.ReviewToRemoveDto;
import com.cozyhome.onlineshop.dto.review.ReviewResponse;
import com.cozyhome.onlineshop.dto.review.ReviewAdminResponse;
import com.cozyhome.onlineshop.dto.review.ReviewRequest;

import java.util.List;

public interface ReviewService {
    List<ReviewAdminResponse> getReviews();

    ReviewResponse addNewReview(ReviewRequest review);

    List<ReviewResponse> getReviewsForProduct(String productSkuCode);

    void removeReviewById(ReviewToRemoveDto reviewRemoveDto);

    List<ReviewAdminResponse> getReviewsForProductAllInf(String productSkuCode);
}
