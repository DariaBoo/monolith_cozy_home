package com.cozyhome.onlineshop.userservice.model;

import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "Role")
public class Role implements GrantedAuthority{
	
	@Id
    private String id;
	@UniqueElements
    private RoleE name;
    
    @Override
	public String getAuthority() {
		return name.toString();
	}
}
