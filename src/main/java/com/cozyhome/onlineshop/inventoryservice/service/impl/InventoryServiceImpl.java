package com.cozyhome.onlineshop.inventoryservice.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cozyhome.onlineshop.dto.inventory.AvailabilityStatusDto;
import com.cozyhome.onlineshop.dto.inventory.ProductAvailabilityDto;
import com.cozyhome.onlineshop.dto.inventory.QuantityStatusDto;
import com.cozyhome.onlineshop.dto.request.ProductColorDto;
import com.cozyhome.onlineshop.inventoryservice.model.Inventory;
import com.cozyhome.onlineshop.inventoryservice.model.enums.ProductQuantityStatus;
import com.cozyhome.onlineshop.inventoryservice.repository.InventoryRepository;
import com.cozyhome.onlineshop.inventoryservice.service.InventoryService;
import com.cozyhome.onlineshop.productservice.model.Product;
import com.cozyhome.onlineshop.productservice.repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {

	private final InventoryRepository inventoryRepository;
	private final ProductRepository productRepository;

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
	public List<ProductAvailabilityDto> getProductAvailableStatus(List<ProductColorDto> productColorDto) {
		List<ProductAvailabilityDto> availabilityInfoList = new ArrayList<>();
		for (ProductColorDto productColor : productColorDto) {
			Optional<Integer> inventory = inventoryRepository.findQuantityByProductSkuCodeAndColorHex(
					productColor.getProductSkuCode(), productColor.getColorHex());

			if (inventory.isPresent()) {
				availabilityInfoList
						.add(new ProductAvailabilityDto(productColor, new AvailabilityStatusDto(inventory.get(),
								ProductQuantityStatus.getStatusByQuantity(inventory.get()))));
				log.info(
						"[ON getProductAvailableStatus] :: Get availableProductQuantity and quantityStatus for product with skuCode = "
								+ productColor.getProductSkuCode() + ", and color hex = " + productColor.getColorHex());
			}
		}

		return availabilityInfoList;
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

	@Override
	public void updateProductAvailability(List<String> productSkuCodeList) {
		List<Inventory> inventoryList = inventoryRepository.findByProductColorProductSkuCodeIn(productSkuCodeList);
		Map<String, List<Inventory>> skuCodeInventoryMap = inventoryList.stream().collect(Collectors
				.groupingBy(inventory -> inventory.getProductColor().getProductSkuCode(), Collectors.toList()));

		for (String skuCode : productSkuCodeList) {
			Optional<Product> product = productRepository.findBySkuCode(skuCode);
			if (product.isPresent()) {
				int quantity = skuCodeInventoryMap.get(skuCode).stream().mapToInt(Inventory::getQuantity).sum();
				if (quantity == 0) {
					product.get().setAvailable(false);
				} else {
					product.get().setAvailable(true);
				}
			}
			productRepository.save(product.get());
			log.info("[ON updateProductAvailability] :: update product availability for product with skucode {}",
					skuCode);
		}
	}

}
