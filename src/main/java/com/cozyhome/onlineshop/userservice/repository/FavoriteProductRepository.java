package com.cozyhome.onlineshop.userservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cozyhome.onlineshop.userservice.model.FavoriteProduct;

public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, Integer>{

	
	Page<FavoriteProduct> findAllByUserId(String userId, Pageable pageable);
	
	List<FavoriteProduct> findAllByUserId(String userId);
	
	Optional<FavoriteProduct> findByProductSkuCodeAndUserId(String productSkuCode, String userId);
	
	boolean existsByProductSkuCodeAndUserId(String productSkuCode, String userId);

}
