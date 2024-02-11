package com.cozyhome.onlineshop.inventoryservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "inventory")
public class Inventory {


	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
	@OneToOne
	@JoinColumn(name = "product_color_id", referencedColumnName = "id")
	private ProductColor productColor;
	
	private int quantity;
}
