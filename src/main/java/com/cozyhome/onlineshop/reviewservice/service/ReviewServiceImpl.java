package com.cozyhome.onlineshop.reviewservice.service;

import com.cozyhome.onlineshop.dto.review.ReviewToRemoveDto;
import com.cozyhome.onlineshop.dto.review.ReviewResponse;
import com.cozyhome.onlineshop.dto.review.ReviewAdminResponse;
import com.cozyhome.onlineshop.dto.review.ReviewRequest;
import com.cozyhome.onlineshop.exception.DataNotFoundException;
import com.cozyhome.onlineshop.reviewservice.model.Review;
import com.cozyhome.onlineshop.reviewservice.repository.ReviewRepository;
import com.cozyhome.onlineshop.reviewservice.service.builder.ReviewBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
	
    private final ReviewRepository repository;
    private final ModelMapper mapper;
    private final ReviewBuilder reviewBuilder;
    
    @Override
    public List<ReviewAdminResponse> getReviews() {
        List<Review> reviews = repository.findAll();
        return reviews.stream().filter(Objects::nonNull).map(review -> mapper.map(review, ReviewAdminResponse.class)).toList();
    }

    @Override
    public ReviewResponse addNewReview(ReviewRequest reviewRequest) {
        Review review = mapper.map(reviewRequest, Review.class);
        review.setCreatedAt(LocalDateTime.now());
        review.setModifiedAt(LocalDateTime.now());
        Review savedReview = repository.save(review);
        return reviewBuilder.buildReviewResponse(savedReview);
    }

    @Override
    public List<ReviewResponse> getReviewsForProduct(String productSkuCode) {
        List<Review> reviews = repository.findReviewsByProductSkuCode(productSkuCode);
        return reviews.stream().filter(Objects::nonNull).map(reviewBuilder::buildReviewResponse).toList();
    }

    @Override
    public void removeReviewById(ReviewToRemoveDto reviewRemoveDto) {
        boolean isExist = repository.existsById(UUID.fromString(reviewRemoveDto.getReviewId()));
        if (isExist) {
            repository.deleteById(UUID.fromString(reviewRemoveDto.getReviewId()));
        } else {
            log.error("[ON removeReviewById]:: Review with id {} doesn't exist.", reviewRemoveDto.getReviewId());
            throw new DataNotFoundException(String.format("[ON removeReviewById]:: Review with id %s doesn't exist.", reviewRemoveDto.getReviewId()));
        }
    }

    @Override
    public List<ReviewAdminResponse> getReviewsForProductAllInf(String productSkuCode) {
        List<Review> reviews = repository.findReviewsByProductSkuCode(productSkuCode);
        return reviews.stream().filter(Objects::nonNull).map(review -> mapper.map(review, ReviewAdminResponse.class)).toList();
    }
}
