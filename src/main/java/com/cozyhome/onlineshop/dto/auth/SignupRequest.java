package com.cozyhome.onlineshop.dto.auth;

import java.util.Set;

import com.cozyhome.onlineshop.validation.ValidEmail;
import com.cozyhome.onlineshop.validation.ValidFirstNameAndLastName;
import com.cozyhome.onlineshop.validation.ValidOptionalFieldBirthday;
import com.cozyhome.onlineshop.validation.ValidPassword;
import com.cozyhome.onlineshop.validation.ValidPhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SignupRequest {

	@ValidEmail
	private String email;
	@ValidPassword
	private String password;
	@ValidFirstNameAndLastName
	private String firstName;
	@ValidFirstNameAndLastName
	private String lastName;
	@ValidOptionalFieldBirthday
	private String birthday;
	@ValidPhoneNumber
	private String phoneNumber;
	private Set<String> roles;

}
