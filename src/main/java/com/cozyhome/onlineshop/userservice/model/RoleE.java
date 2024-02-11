package com.cozyhome.onlineshop.userservice.model;

import java.util.HashMap;
import java.util.Map;

import com.cozyhome.onlineshop.productservice.model.enums.ProductStatus;

public enum RoleE {

	ROLE_ADMIN("admin"),
	ROLE_CUSTOMER("customer"),
	ROLE_MANAGER("manager");
	
	private final String roleName;
	private static final Map<String, RoleE> ROLE_MAP = new HashMap<>();
	
	RoleE(String roleName) {
		this.roleName = roleName;
	}
	
	static {
        for (RoleE element : values()) {
        	ROLE_MAP.put(element.roleName, element);
        }
    }

    public static RoleE getByRoleName(String roleName) {
        return ROLE_MAP.get(roleName);
    }
}
