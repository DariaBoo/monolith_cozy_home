package com.cozyhome.onlineshop.dto.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductSkuCodeRequest {

	private List<String> productSkuCodeList;
}
