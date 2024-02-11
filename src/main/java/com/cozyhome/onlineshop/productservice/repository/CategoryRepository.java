package com.cozyhome.onlineshop.productservice.repository;

import com.cozyhome.onlineshop.productservice.model.Category;
import com.cozyhome.onlineshop.productservice.wrapper.IdWrapper;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, ObjectId> {

    Optional<Category> findByNameAndActive(String name, boolean active);

    List<Category> findAllByActiveAndParentIdNull(boolean active);

    @Query(value = "{active: ?0, parentId: ?1}", fields = "{_id: 1}")
    List<IdWrapper> findAllIdsOnlyByActiveAndParentId(boolean active, ObjectId parentId);

    List<Category> findAllByActiveAndIdIn(boolean active, List<ObjectId> id);
    
    List<Category> findAllByActive(boolean active);
    
    @Query(value = "{'_id': ?0, 'parentId': {$exists: true}}", exists = true)
    boolean hasParentById(ObjectId categoryId);
    
    Optional<Category> findById(ObjectId id);
}
