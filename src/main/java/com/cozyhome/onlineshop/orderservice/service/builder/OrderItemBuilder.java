package com.cozyhome.onlineshop.orderservice.service.builder;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.cozyhome.onlineshop.dto.order.OrderItemDto;
import com.cozyhome.onlineshop.exception.DataNotFoundException;
import com.cozyhome.onlineshop.exception.ProductOutOfStockException;
import com.cozyhome.onlineshop.inventoryservice.model.Inventory;
import com.cozyhome.onlineshop.inventoryservice.repository.InventoryRepository;
import com.cozyhome.onlineshop.inventoryservice.service.InventoryService;
import com.cozyhome.onlineshop.orderservice.model.OrderItem;
import com.cozyhome.onlineshop.orderservice.repository.OrderItemRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderItemBuilder {

    private final InventoryRepository inventoryRepository;
    private final OrderItemRepository orderItemRepository;

    public List<OrderItem> buildOrderItems(List<OrderItemDto> orderItems) {
    	return orderItems.stream().map(this::buildOrderItem).toList();
        
    }

    private OrderItem buildOrderItem(OrderItemDto orderItemDto) {
        Inventory inventory = inventoryRepository.findByProductColorProductSkuCodeAndProductColorColorHex(
                orderItemDto.getProductSkuCode(), orderItemDto.getColorHex())
                .orElseThrow(() -> new DataNotFoundException(String.format("Product with sku code %s and color %s " +
                        "doesn't found", orderItemDto.getProductSkuCode(), orderItemDto.getColorHex())));
        
        if (inventory.getQuantity() < orderItemDto.getQuantity()) {
         throw new ProductOutOfStockException(String.format("Not enough quantity to order product with sku code %s and " +
                 "color hex %s.", orderItemDto.getProductSkuCode(), orderItemDto.getColorHex()));
        } 
        
        inventory.setQuantity(inventory.getQuantity() - orderItemDto.getQuantity());
        inventoryRepository.save(inventory);
        
        OrderItem orderItem = OrderItem.builder()
                .productColorId(inventory.getProductColor().getId())
                .quantity(orderItemDto.getQuantity())
                .price(new BigDecimal(orderItemDto.getPrice()))
                .build();
        return orderItemRepository.save(orderItem);
    }
   
}
