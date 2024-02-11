package com.cozyhome.onlineshop.orderservice.repository;

import com.cozyhome.onlineshop.orderservice.model.Delivery;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeliveryRepository extends MongoRepository<Delivery, ObjectId> {
}
