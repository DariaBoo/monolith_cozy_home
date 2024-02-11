package com.cozyhome.onlineshop.orderservice.service;

import com.cozyhome.onlineshop.dto.order.OrderDto;
import com.cozyhome.onlineshop.dto.order.OrderNumberDto;

public interface OrderService {
    OrderNumberDto createOrder(OrderDto orderDto, String userId);

}
