package com.cozyhome.onlineshop.userservice.security.service;

import com.cozyhome.onlineshop.userservice.model.User;

public interface SecurityTokenService {
	
	void createActivationUserToken(User user);
	
	void createPasswordResetToken(String userEmail, String ipAddress);

}
