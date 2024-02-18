package com.cozyhome.onlineshop.inventoryservice.controller;

import com.cozyhome.onlineshop.dto.inventory.ProductAvailabilityDto;
import com.cozyhome.onlineshop.dto.inventory.QuantityStatusDto;
import com.cozyhome.onlineshop.dto.request.ProductColorDto;
import com.cozyhome.onlineshop.inventoryservice.service.InventoryService;
import com.cozyhome.onlineshop.validation.ValidSkuCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/inventory")
@RestController
public class InventoryController {
	private final InventoryService inventoryService;
	
	//for product card
	@GetMapping("/color-quantity-status/product-skuCode")
	public ResponseEntity<QuantityStatusDto> getColorQuantityStatusBySkuCode(@RequestParam @ValidSkuCode String productSkuCode){
		return ResponseEntity.ok(inventoryService.getProductCardColorQuantityStatus(productSkuCode));
	}	
	
	//for preview
	@PostMapping("/quantity-status-map/product-skuCode-list")
	public ResponseEntity<Map<String, QuantityStatusDto>> getProductQuantityStatusMap(@RequestBody List<String> productSkuCodeList){
		return ResponseEntity.ok(inventoryService.getQuantityStatusBySkuCodeList(productSkuCodeList));
	}

	// for basket
	@PostMapping("/shopping-card-info")
	public ResponseEntity<List<ProductAvailabilityDto>> getProductAvailableStatus(@RequestBody List<ProductColorDto> productColorDto) {
		return ResponseEntity.ok(inventoryService.getProductAvailableStatus(productColorDto));
	}
}
