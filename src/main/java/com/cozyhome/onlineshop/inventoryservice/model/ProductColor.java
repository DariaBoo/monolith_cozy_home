package com.cozyhome.onlineshop.inventoryservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
@Table(name = "product_color")
public class ProductColor {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Exclude
    private int id;

	@Column(name = "product_skucode")
    private String productSkuCode;
	
	@Column(name = "color_hex")
    private String colorHex;
	
	@JsonIgnore
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@OneToOne(mappedBy = "productColor")
	private Inventory inventory;
	
	public ProductColor(String productSkuCode, String colorHex) {
		this.productSkuCode = productSkuCode;
		this.colorHex = colorHex;
	}
}
