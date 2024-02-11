package com.cozyhome.onlineshop.userservice.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cozyhome.onlineshop.userservice.model.token.SecurityToken;

@Repository
public interface SecurityTokenRepository extends MongoRepository<SecurityToken, ObjectId> {
	
	SecurityToken findByToken(String token);

}
