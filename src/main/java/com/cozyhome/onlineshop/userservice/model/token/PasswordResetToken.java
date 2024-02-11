package com.cozyhome.onlineshop.userservice.model.token;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Document(collection = "Token")
public class PasswordResetToken extends SecurityToken {
	
	private String ipAddress;

}
