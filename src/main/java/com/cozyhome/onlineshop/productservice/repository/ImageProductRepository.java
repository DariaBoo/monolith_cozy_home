package com.cozyhome.onlineshop.productservice.repository;

import com.cozyhome.onlineshop.productservice.model.ImageProduct;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ImageProductRepository extends MongoRepository<ImageProduct, ObjectId> {

    Optional<ImageProduct> findByProductSkuCodeAndColorIdAndMainPhotoTrue(String productSkuCode, String colorId);

    List<ImageProduct> findByProductSkuCodeAndColorId(String productSkuCode, String colorId);

}
