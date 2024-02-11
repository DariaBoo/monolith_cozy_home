package com.cozyhome.onlineshop.userservice.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.cozyhome.onlineshop.userservice.model.Role;
import com.cozyhome.onlineshop.userservice.model.RoleE;

public interface RoleRepository extends MongoRepository<Role, ObjectId>{
	
	 Optional<Role> getByName(RoleE name);

}
