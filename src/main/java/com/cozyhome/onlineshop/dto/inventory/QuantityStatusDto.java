package com.cozyhome.onlineshop.dto.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class QuantityStatusDto {

	private String status;
	private Map<String, String> colorQuantityStatus;
}
