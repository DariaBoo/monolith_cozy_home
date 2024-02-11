package com.cozyhome.onlineshop.productservice.repository;

import com.cozyhome.onlineshop.productservice.model.ImageCategory;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ImageCategoryRepository extends MongoRepository<ImageCategory, ObjectId> {
	
    List<ImageCategory> findAllByCatalogAndCategoryIn(boolean catalog, List<ObjectId> ids);

    List<ImageCategory> findAllByCatalogFalse();
}
