package com.cozyhome.onlineshop.dto.auth;

import com.cozyhome.onlineshop.validation.ValidName;
import com.cozyhome.onlineshop.validation.ValidOptionalBirthday;
import com.cozyhome.onlineshop.validation.ValidPassword;
import com.cozyhome.onlineshop.validation.ValidPhoneNumber;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CustomSignupRequest extends SignupRequest {

	@ValidPassword
	private String password;
	@ValidName
	private String lastName;
	@ValidOptionalBirthday
	private String birthday;
	@ValidPhoneNumber
	private String phoneNumber;	
	
}
