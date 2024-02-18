package com.cozyhome.onlineshop.orderservice.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cozyhome.onlineshop.dto.EmailMessageDto;
import com.cozyhome.onlineshop.dto.order.OrderDto;
import com.cozyhome.onlineshop.dto.order.OrderItemDto;
import com.cozyhome.onlineshop.dto.order.OrderNumberDto;
import com.cozyhome.onlineshop.emailservice.EmailService;
import com.cozyhome.onlineshop.exception.DataNotFoundException;
import com.cozyhome.onlineshop.inventoryservice.model.Inventory;
import com.cozyhome.onlineshop.inventoryservice.model.ProductColor;
import com.cozyhome.onlineshop.inventoryservice.repository.InventoryRepository;
import com.cozyhome.onlineshop.inventoryservice.repository.ProductColorRepository;
import com.cozyhome.onlineshop.inventoryservice.service.InventoryService;
import com.cozyhome.onlineshop.orderservice.model.Delivery;
import com.cozyhome.onlineshop.orderservice.model.Order;
import com.cozyhome.onlineshop.orderservice.model.OrderItem;
import com.cozyhome.onlineshop.orderservice.model.enums.OrderStatus;
import com.cozyhome.onlineshop.orderservice.repository.OrderRepository;
import com.cozyhome.onlineshop.orderservice.service.OrderService;
import com.cozyhome.onlineshop.orderservice.service.builder.DeliveryBuilder;
import com.cozyhome.onlineshop.orderservice.service.builder.OrderItemBuilder;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final OrderItemBuilder orderItemBuilder;
	private final DeliveryBuilder deliveryBuilder;
	private final EmailService emailService;
	private final InventoryRepository inventoryRepository;
	private final InventoryService inventoryService;
	private final ProductColorRepository productColorRepository;

	@Value("${order.message.subject}")
	private String activationEmailSubject;

	@Value("${new_order.message.text}")
	private String activationEmailText;

	@Value("${order.message.subject}")
	private String orderMessageSubject;

	@Value("${new_order.message.text}")
	private String newOrderMessageText;

	@Value("${processed_order.message.text}")
	private String processedOrderMessageText;

	@Value("${shipped_order.message.text}")
	private String shippedOrderMessageText;

	@Value("${delivered_order.message.text}")
	private String deliveredOrderMessageText;

	@Value("${change_order_status.message.text}")
	private String changeOrderStatusMessageText;

	@Override
	public OrderNumberDto createOrder(OrderDto orderDto, String userId) {

		List<OrderItem> orderItems = orderItemBuilder.buildOrderItems(orderDto.getOrderItems());
		inventoryService.updateProductAvailability(
				orderDto.getOrderItems().stream().map(OrderItemDto::getProductSkuCode).toList());

		Order order = Order.builder()
				.firstName(orderDto.getFirstName())
				.lastName(orderDto.getLastName())
				.phoneNumber(orderDto.getPhoneNumber())
				.orderItem(orderItems)
				.orderStatus(OrderStatus.NEW)
				.createdAt(LocalDateTime.now())
				.modifiedAt(LocalDateTime.now())
				.build();

		int orderNumber = getOrderNumber();

		Delivery delivery;
		if (orderDto.getDelivery().getDeliveryCompanyName() == null
				|| orderDto.getDelivery().getDeliveryCompanyName().isEmpty()) {
			delivery = deliveryBuilder.buildAddressDelivery(orderDto.getDelivery());
		} else {
			delivery = deliveryBuilder.buildPostalDelivery(orderDto.getDelivery());
		}
		order.setDelivery(delivery);

		if (orderDto.getEmail() != null && !orderDto.getEmail().isEmpty()) {
			order.setEmail(orderDto.getEmail());
			sendStatusMessage(orderDto.getEmail(), OrderStatus.NEW.toString(), String.valueOf(orderNumber));
		}
		if (userId != null) {
			order.setUserId(userId);
		}

		order.setOrderNumber(orderNumber);
		orderRepository.save(order);
		log.info("[ON createOrder] :: The order with number {} is saved.", orderNumber);
		return new OrderNumberDto(orderNumber);
	}

	private void sendStatusMessage(String email, String status, String orderNumber) {
		EmailMessageDto activationEmail = EmailMessageDto.builder().mailTo(email)
				.subject(orderMessageSubject.replace("{0}", orderNumber)).build();
		if (status.equalsIgnoreCase(OrderStatus.NEW.toString())) {
			activationEmail.setText(newOrderMessageText);
		} else if (status.equalsIgnoreCase(OrderStatus.PROCESSED.toString())) {
			activationEmail.setText(processedOrderMessageText);
		} else if (status.equalsIgnoreCase(OrderStatus.SHIPPED.toString())) {
			activationEmail.setText(shippedOrderMessageText);
		} else if (status.equalsIgnoreCase(OrderStatus.DELIVERED.toString())) {
			activationEmail.setText(deliveredOrderMessageText);
		} else {
			activationEmail.setText(changeOrderStatusMessageText);
		}

		emailService.sendEmail(activationEmail);
	}

	private Integer getOrderNumber() {
		int orderNumber;
		Optional<Order> order = orderRepository.findFirstByOrderByOrderNumberDesc();
		if (order.isPresent()) {
			orderNumber = order.get().getOrderNumber();
			orderNumber++;
			orderNumber = checkOrderNumberExists(orderNumber);
		} else {
			orderNumber = getRandomOrderNumber();
			orderNumber = checkOrderNumberExists(orderNumber);
		}
		return orderNumber;
	}

	private Integer getRandomOrderNumber() {
		Random rand = new Random();
		int maxValue = 10000;
		int minValue = 1000;
		return rand.nextInt(maxValue - minValue + 1) + minValue;
	}

	private int checkOrderNumberExists(int orderNumber) {
		boolean isExist = orderRepository.existsByOrderNumber(orderNumber);
		while (isExist) {
			orderNumber++;
			isExist = orderRepository.existsByOrderNumber(orderNumber);
		}
		return orderNumber;
	}

	public void deleteOrder(Order order) {
		for (OrderItem orderItem : order.getOrderItem()) {
			Inventory inventory = inventoryRepository.findByProductColorId(orderItem.getProductColorId())
					.orElseThrow(() -> new DataNotFoundException("Product with sku code and color is not found"));
			int updatedQuantity = inventory.getQuantity() + orderItem.getQuantity();
			inventory.setQuantity(updatedQuantity);
			inventoryRepository.save(inventory);
			log.info("[ON deleteOrder] :: set new quantity for order with number {}", order.getOrderNumber());
			orderRepository.delete(order);
			log.info("[ON deleteOrder] :: delete order, status {}",
					orderRepository.existsByOrderNumber(order.getOrderNumber()));
		}
	}

	public void deleteByEmail(String email) {
		List<Order> orderList = orderRepository.getByEmail(email);
		log.info("[ON deleteByEmail] :: Get a list of orders {} by email {}", orderList, email);
		for (Order order : orderList) {
			deleteOrder(order);
			List<ProductColor> productColorList = productColorRepository.findByIdIn(order.getOrderItem().stream().map(OrderItem::getProductColorId).toList());
			inventoryService.updateProductAvailability(productColorList.stream().map(ProductColor::getProductSkuCode).toList());
		}
	}
}
