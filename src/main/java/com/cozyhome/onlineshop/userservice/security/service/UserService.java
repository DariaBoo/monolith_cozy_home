package com.cozyhome.onlineshop.userservice.security.service;

import com.cozyhome.onlineshop.dto.auth.NewPasswordRequest;
import com.cozyhome.onlineshop.dto.auth.SignupRequest;
import com.cozyhome.onlineshop.dto.user.PasswordUpdateRequest;
import com.cozyhome.onlineshop.dto.user.UserInformationRequest;
import com.cozyhome.onlineshop.dto.user.UserInformationResponse;
import com.cozyhome.onlineshop.userservice.model.User;

public interface UserService {
	
	void saveUser(SignupRequest signupRequest);

	boolean existsByEmail(String email);
	
	User activateUser(String activationToken);
	
	User resetPassword(String token, NewPasswordRequest newPassword);

	UserInformationResponse updateUserData(UserInformationRequest userInformationDto, String userId);

	void updateUserPassword(PasswordUpdateRequest passwords, String userId);

	UserInformationResponse getUserInfo(String userId);

	void deleteUser(String email);
	
}
