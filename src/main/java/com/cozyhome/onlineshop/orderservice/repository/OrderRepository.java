package com.cozyhome.onlineshop.orderservice.repository;

import com.cozyhome.onlineshop.orderservice.model.Order;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, ObjectId> {

    Optional<Order> findFirstByOrderByOrderNumberDesc();    

    boolean existsByOrderNumber(int orderNumber);
    
    Optional<Order> findByOrderNumber(int orderNumber);
    
    List<Order> getByEmail(String email);  
}
