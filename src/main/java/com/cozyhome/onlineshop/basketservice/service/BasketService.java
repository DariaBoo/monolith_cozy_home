package com.cozyhome.onlineshop.basketservice.service;

import java.util.List;

import com.cozyhome.onlineshop.dto.basket.BasketDto;
import com.cozyhome.onlineshop.dto.basket.BasketItemDto;

public interface BasketService {
	
	List<BasketDto> getBasket(String userId);
	
	List<BasketDto> mergeUserBaskets(String userId, List<BasketItemDto> dtoList);
	
	void replaceBasket(String userId, List<BasketItemDto> dtoList);

}
