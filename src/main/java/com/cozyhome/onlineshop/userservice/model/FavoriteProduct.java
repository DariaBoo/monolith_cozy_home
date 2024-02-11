package com.cozyhome.onlineshop.userservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "favorite_products")
public class FavoriteProduct {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Exclude
	private int id;
	
	@Column(name = "product_skucode")
	private String productSkuCode;
	
	@Column(name = "user_id")
	private String userId;
	
	public FavoriteProduct(String productSkuCode, String userId) {
		this.productSkuCode = productSkuCode;
		this.userId = userId;
	}
}
