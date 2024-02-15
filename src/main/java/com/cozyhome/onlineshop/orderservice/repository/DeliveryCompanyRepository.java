package com.cozyhome.onlineshop.orderservice.repository;

import com.cozyhome.onlineshop.orderservice.model.DeliveryCompany;
import com.cozyhome.onlineshop.orderservice.model.enums.EntityStatus;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryCompanyRepository extends MongoRepository<DeliveryCompany, ObjectId> {

    List<DeliveryCompany> findAllByStatus(EntityStatus status);
    
    boolean existsByNameAndStatus(String companyName, EntityStatus status);
    
    Optional<DeliveryCompany> findByNameAndStatus(String name, EntityStatus status);
}
