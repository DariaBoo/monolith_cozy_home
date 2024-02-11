package com.cozyhome.onlineshop.dto.auth;

import java.util.Set;

import com.cozyhome.onlineshop.validation.ValidEmail;
import com.cozyhome.onlineshop.validation.ValidName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

	@ValidEmail
	private String email;	
	@ValidName
	private String firstName;
	private Set<String> roles;
}
