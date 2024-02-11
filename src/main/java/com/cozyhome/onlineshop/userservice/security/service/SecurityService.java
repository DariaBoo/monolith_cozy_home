package com.cozyhome.onlineshop.userservice.security.service;

public interface SecurityService {

	boolean isAuthenticated(String username, String password);
	
	boolean isActivated(String username);
}
