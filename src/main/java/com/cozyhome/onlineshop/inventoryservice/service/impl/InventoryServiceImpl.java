package com.cozyhome.onlineshop.inventoryservice.service.impl;

import com.cozyhome.onlineshop.dto.inventory.ProductAvailabilityDto;
import com.cozyhome.onlineshop.dto.inventory.InventoryForBasketDto;
import com.cozyhome.onlineshop.dto.request.ProductColorDto;
import com.cozyhome.onlineshop.dto.inventory.QuantityStatusDto;
import com.cozyhome.onlineshop.inventoryservice.model.Inventory;
import com.cozyhome.onlineshop.inventoryservice.model.enums.ProductQuantityStatus;
import com.cozyhome.onlineshop.inventoryservice.repository.InventoryRepository;
import com.cozyhome.onlineshop.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class InventoryServiceImpl implements InventoryService {
	private final InventoryRepository inventoryRepository;

	@Override
	public int getQuantityByProductColor(ProductColorDto request) {
		int quantity = 0;
		Optional<Inventory> inventory = inventoryRepository.findByProductColorProductSkuCodeAndProductColorColorHex(
				request.getProductSkuCode(), request.getColorHex());
		if (inventory.isPresent()) {
			quantity = inventory.get().getQuantity();
			log.info("[ON getQuantityByProductColor] :: get quantity [" + quantity + "] for product skuCode ["
					+ request.getProductSkuCode() + "] and color hex [" + request.getColorHex() + "].");
		}
		return quantity;
	}

	@Override
	public String getQuantityStatusByProductColor(ProductColorDto request) {
		int productQuantity = getQuantityByProductColor(request);
		return ProductQuantityStatus.getStatusByQuantity(productQuantity);
	}

	@Override
	public Map<String, QuantityStatusDto> getQuantityStatusBySkuCodeList(List<String> productSkuCodeList) {
		List<Inventory> inventoryList = inventoryRepository.findByProductColorProductSkuCodeIn(productSkuCodeList);
		Map<String, QuantityStatusDto> skuCodeQuantityStatusMap = new HashMap<>();
		Map<String, List<Inventory>> skuCodeInventoryMap = inventoryList.stream().collect(Collectors
				.groupingBy(inventory -> inventory.getProductColor().getProductSkuCode(), Collectors.toList()));

		for (String skuCode : productSkuCodeList) {
			List<Inventory> inventories = skuCodeInventoryMap.get(skuCode);
			skuCodeQuantityStatusMap.put(skuCode, createQuantityStatusDto(inventories));
		}
		return skuCodeQuantityStatusMap;
	}

	@Override
	public QuantityStatusDto getProductCardColorQuantityStatus(String productSkuCode) {
		List<Inventory> inventories = inventoryRepository.findByProductColorProductSkuCode(productSkuCode);
		return createQuantityStatusDto(inventories);
	}

	@Override
	public List<InventoryForBasketDto> getProductAvailableStatus(List<ProductColorDto> productColorDto) {
		List<InventoryForBasketDto> checkAvailableAndStatus = new ArrayList<>();
		for (ProductColorDto productColor : productColorDto) {
			Optional<Integer> inventory = inventoryRepository.findQuantityByProductSkuCodeAndColorHex(
					productColor.getProductSkuCode(), productColor.getColorHex());

			if (inventory.isPresent()) {
				checkAvailableAndStatus
						.add(new InventoryForBasketDto(productColor, new ProductAvailabilityDto(inventory.get(),
								ProductQuantityStatus.getStatusByQuantity(inventory.get()))));
				log.info("[ON getProductAvailableStatus] :: Get availableProductQuantity and quantityStatus for product with skuCode = "
						+ productColor.getProductSkuCode() + ", and color hex = " + productColor.getColorHex());
			}
		}
		
		return checkAvailableAndStatus;
	}

	private QuantityStatusDto createQuantityStatusDto(List<Inventory> inventories) {
		Map<String, String> colorHexStatus = new HashMap<>();
		int quantity = inventories.stream().mapToInt(Inventory::getQuantity).sum();
		String generalStatus = ProductQuantityStatus.getStatusByQuantity(quantity);

		for (Inventory inventory : inventories) {
			String status = ProductQuantityStatus.getStatusByQuantity(inventory.getQuantity());
			colorHexStatus.put(inventory.getProductColor().getColorHex(), status);
		}

		return new QuantityStatusDto(generalStatus, colorHexStatus);
	}
}
