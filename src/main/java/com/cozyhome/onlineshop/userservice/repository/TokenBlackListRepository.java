package com.cozyhome.onlineshop.userservice.repository;

import com.cozyhome.onlineshop.userservice.model.TokenBlackList;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenBlackListRepository extends MongoRepository<TokenBlackList, String> {
}
