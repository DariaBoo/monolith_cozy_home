package com.cozyhome.onlineshop.userservice.security.service;

public interface TokenBlackListService {

	void saveTokenToBlackList(String jwtToken);

}
