package com.cozyhome.onlineshop.orderservice.repository;

import com.cozyhome.onlineshop.orderservice.model.OrderItem;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderItemRepository extends MongoRepository<OrderItem, ObjectId> {
}
