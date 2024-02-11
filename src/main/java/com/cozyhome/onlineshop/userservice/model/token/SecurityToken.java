package com.cozyhome.onlineshop.userservice.model.token;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.cozyhome.onlineshop.userservice.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@Document(collection = "Token")
public class SecurityToken {
	
	@Id
	private String id;
	
	private String token;
	
	private User user;
	
	private LocalDateTime expiration;
	
	private TokenTypeE tokenType;
	
	public boolean isExpired() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return currentDateTime.isAfter(expiration);
    }
}
