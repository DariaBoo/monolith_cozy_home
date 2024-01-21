package com.cozyhome.onlineshop.userservice.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cozyhome.onlineshop.userservice.model.User;
import com.cozyhome.onlineshop.userservice.model.UserStatusE;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {

	Optional<User> findByEmailAndStatus(String email, UserStatusE status);

	Optional<User> findById(String id);
	
	boolean existsByEmailAndStatus(String username, UserStatusE status);

	Optional<User> findByIdAndStatus(String userId, UserStatusE status);
}
