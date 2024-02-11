package com.cozyhome.onlineshop.dto.auth;

import com.cozyhome.onlineshop.validation.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmailRequest {
	@ValidEmail
	private String email;

}
