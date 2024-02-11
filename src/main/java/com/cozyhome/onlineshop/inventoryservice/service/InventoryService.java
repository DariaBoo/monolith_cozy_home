package com.cozyhome.onlineshop.inventoryservice.service;

import com.cozyhome.onlineshop.dto.inventory.InventoryForBasketDto;
import com.cozyhome.onlineshop.dto.request.ProductColorDto;
import com.cozyhome.onlineshop.dto.inventory.QuantityStatusDto;

import java.util.List;
import java.util.Map;

public interface InventoryService {
	
	int getQuantityByProductColor(ProductColorDto request);

	String getQuantityStatusByProductColor(ProductColorDto request);
	
	Map<String, QuantityStatusDto> getQuantityStatusBySkuCodeList(List<String> productSkuCodeList);
	
	QuantityStatusDto getProductCardColorQuantityStatus(String productSkuCode);

	List<InventoryForBasketDto> getProductAvailableStatus(List<ProductColorDto> productColorDto);
}
