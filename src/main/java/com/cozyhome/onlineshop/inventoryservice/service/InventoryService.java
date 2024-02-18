package com.cozyhome.onlineshop.inventoryservice.service;

import java.util.List;
import java.util.Map;

import com.cozyhome.onlineshop.dto.inventory.ProductAvailabilityDto;
import com.cozyhome.onlineshop.dto.inventory.QuantityStatusDto;
import com.cozyhome.onlineshop.dto.request.ProductColorDto;

public interface InventoryService {
	
	int getQuantityByProductColor(ProductColorDto request);

	String getQuantityStatusByProductColor(ProductColorDto request);
	
	Map<String, QuantityStatusDto> getQuantityStatusBySkuCodeList(List<String> productSkuCodeList);
	
	QuantityStatusDto getProductCardColorQuantityStatus(String productSkuCode);

	List<ProductAvailabilityDto> getProductAvailableStatus(List<ProductColorDto> productColorDto);
	
	void updateProductAvailability(List<String> productSkuCodeList);
}
