package com.cozyhome.onlineshop.reviewservice.service.builder;

import com.cozyhome.onlineshop.dto.review.ReviewResponse;
import com.cozyhome.onlineshop.exception.DataNotFoundException;
import com.cozyhome.onlineshop.reviewservice.model.Review;
import com.cozyhome.onlineshop.userservice.model.User;
import com.cozyhome.onlineshop.userservice.model.UserStatusE;
import com.cozyhome.onlineshop.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewBuilder {
    private final UserRepository userRepository;

    public ReviewResponse buildReviewResponse(Review review) {
//        String email = review.getEmail();
//        User user = userRepository.findByEmailAndStatus(email, UserStatusE.ACTIVE)
//                .orElseThrow(() -> new DataNotFoundException(String.format("User with email = %s doesn't found", email)));
        log.info("[ON getReviewsForProductAllInf]:: build review with id: {} for user: {}.", review.getId(), review.getEmail());
        return ReviewResponse.builder()
                    .reviewId(review.getId().toString())
                    .rating(review.getRating())
                    .userName(review.getUserName())
                    .review(review.getComment())
                    .data(review.getModifiedAt().toLocalDate())
                    .build();
    }
}
