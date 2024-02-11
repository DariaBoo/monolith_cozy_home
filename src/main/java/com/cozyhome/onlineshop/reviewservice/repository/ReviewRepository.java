package com.cozyhome.onlineshop.reviewservice.repository;

import com.cozyhome.onlineshop.reviewservice.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findReviewsByProductSkuCode(String productSkuCode);
}
