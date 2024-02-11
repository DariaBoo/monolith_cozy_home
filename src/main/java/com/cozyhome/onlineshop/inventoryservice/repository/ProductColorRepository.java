package com.cozyhome.onlineshop.inventoryservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cozyhome.onlineshop.inventoryservice.model.ProductColor;

@Repository
public interface ProductColorRepository extends JpaRepository<ProductColor, Integer>{
	
	Optional<ProductColor> findByProductSkuCodeAndColorHex(String productSkuCode, String colorHex);

}
