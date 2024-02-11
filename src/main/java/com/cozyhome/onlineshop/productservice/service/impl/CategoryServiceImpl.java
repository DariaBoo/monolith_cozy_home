package com.cozyhome.onlineshop.productservice.service.impl;

import com.cozyhome.onlineshop.dto.CategoryWithIconDto;
import com.cozyhome.onlineshop.dto.CategoryWithPhotoDto;
import com.cozyhome.onlineshop.dto.CategoryWithSubCategoriesDto;
import com.cozyhome.onlineshop.productservice.model.Category;
import com.cozyhome.onlineshop.productservice.model.ImageCategory;
import com.cozyhome.onlineshop.productservice.repository.CategoryRepository;
import com.cozyhome.onlineshop.productservice.repository.ImageCategoryRepository;
import com.cozyhome.onlineshop.productservice.service.CategoryService;
import com.cozyhome.onlineshop.productservice.service.builder.CategoryBuilder;
import com.cozyhome.onlineshop.productservice.wrapper.IdWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;
    private final ImageCategoryRepository imageCategoryRepository;
    private final CategoryBuilder categoryBuilder;
	private final ModelMapper modelMapper;

	@Override
	public List<CategoryWithIconDto> getCategoryWithIcon() {
		List<Category> categories = categoryRepository.findAllByActiveAndParentIdNull(true);
		if (categories.isEmpty()) {
			log.info("[ON getCategoryWithIcon]:: Categories not found.");
			return new ArrayList<>();
		}
		return categories.stream().map(category -> modelMapper.map(category, CategoryWithIconDto.class)).toList();
	}

	@Override
	public List<ObjectId> getCategoriesIdsByParentId(String parentId) {
		List<ObjectId> ids = categoryRepository.findAllIdsOnlyByActiveAndParentId(true, new ObjectId(parentId)).stream()
				.map(IdWrapper::getId).toList();
		return ids;
	}

    @Override
    public List<CategoryWithSubCategoriesDto> getCategoriesWithPhoto() {
        List<Category> categories = categoryRepository.findAllByActive(true);
		if (categories.isEmpty()) {
			log.error("[ON getCategoriesWithPhoto]:: Categories not found.");
			return new ArrayList<>();
		}
        List<ObjectId> ids = categories.stream().map(Category::getId).toList();
        List<ImageCategory> images = imageCategoryRepository.findAllByCatalogAndCategoryIn(true, ids);
        return categoryBuilder.buildParentCategoryWithCategoriesDtoList(categories, images);
    }

	@Override
	public List<CategoryWithPhotoDto> getCategoriesForHomepage() {
		List<ImageCategory> imageCategories = imageCategoryRepository.findAllByCatalogFalse();
		if (imageCategories.isEmpty()) {
			log.info("[ON getCategoriesForHomepage]:: Images for categories not found.");
			return new ArrayList<>();
		}
		return imageCategories.stream().map(categoryBuilder::mapToCategoryForHomepageDto).toList();
	}

}
