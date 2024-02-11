package com.cozyhome.onlineshop.productservice.service;

import com.cozyhome.onlineshop.dto.CategoryWithIconDto;
import com.cozyhome.onlineshop.dto.CategoryWithPhotoDto;
import com.cozyhome.onlineshop.dto.CategoryWithSubCategoriesDto;
import org.bson.types.ObjectId;

import java.util.List;

public interface CategoryService {
	
    List<CategoryWithIconDto> getCategoryWithIcon();
    
    List<ObjectId> getCategoriesIdsByParentId(String parentId);
    
    List<CategoryWithSubCategoriesDto> getCategoriesWithPhoto();
    
    List<CategoryWithPhotoDto> getCategoriesForHomepage();
}
