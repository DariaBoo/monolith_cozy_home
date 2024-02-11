package com.cozyhome.onlineshop.basketservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cozyhome.onlineshop.basketservice.model.BasketItem;

import jakarta.transaction.Transactional;

public interface BasketRepository extends JpaRepository<BasketItem, Integer>{
	
	@Transactional
	List<BasketItem> findByUserId(String userId);

	@Transactional
	void deleteAllByUserId(String userId);
}
