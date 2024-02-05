package com.cozyhome.onlineshop.productservice.repository.impl;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sample;

import java.util.List;

import org.bson.types.Decimal128;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.cozyhome.onlineshop.dto.filter.FilterDto;
import com.cozyhome.onlineshop.dto.filter.ParametersDto;
import com.cozyhome.onlineshop.productservice.model.Product;
import com.cozyhome.onlineshop.productservice.model.enums.MaxLoadEnum;
import com.cozyhome.onlineshop.productservice.model.enums.ProductStatus;
import com.cozyhome.onlineshop.productservice.repository.ProductRepositoryCustom;
import com.cozyhome.onlineshop.productservice.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

	private final MongoTemplate mongoTemplate;
	private final CategoryService categoryService;

	@Override
	public List<Product> getRandomByStatusAndInStock(ProductStatus status, int productCount) {
		Aggregation aggregation = Aggregation.newAggregation(match(Criteria.where("status").is(status)),
				match(Criteria.where("available").is(true)), sample(productCount));

		List<Product> result = mongoTemplate.aggregate(aggregation, Product.class, Product.class).getMappedResults();
		return result;
	}

	@Override
	public List<Product> getRandomByStatusAndCategoryIdAndInStock(ProductStatus status, List<ObjectId> categoriesIds,
			int count) {
		Aggregation aggregation = Aggregation.newAggregation(match(Criteria.where("subCategory").in(categoriesIds)),
				match(Criteria.where("status").is(status)), match(Criteria.where("available").is(true)), sample(count));

		List<Product> result = mongoTemplate.aggregate(aggregation, Product.class, Product.class).getMappedResults();
		return result;
	}

	@Override
	public Page<Product> getBySkuCodeInAndCategoryIdsIn(List<String> skuCodesList, List<ObjectId> categoryIdsList, Pageable page) {
	    if (skuCodesList.isEmpty() || categoryIdsList.isEmpty()) {
	        return Page.empty(); 
	    }

	    Criteria criteria = Criteria.where("skuCode").in(skuCodesList).and("subCategory.$id").in(categoryIdsList);
	    Query query = new Query(criteria);

	    long count = mongoTemplate.count(query, Product.class);

	    List<Product> resultList = mongoTemplate.find(query.with(page), Product.class);

	    return PageableExecutionUtils.getPage(resultList, page, () -> count);
	}
	
	@Override
	public List<Product> filterProductsByCriterias(FilterDto filter, Pageable page) {	

		final Query query = page == null ? new Query() : new Query().with(page);
		
		if(filter.getKeyWord() != null) {
			final String pattern = ".*" + filter.getKeyWord() + ".*";
			final String caseInsensitive = "i";
			Criteria skuCodeCriteria = Criteria.where("skuCode").regex(pattern);		
		    Criteria nameCriteria = Criteria.where("name").regex(pattern, caseInsensitive);
		    query.addCriteria(new Criteria().orOperator(skuCodeCriteria, nameCriteria));
		}

		if (filter.getParentCategoryId() == null) {
			throw new IllegalArgumentException("ParentCategoryId is required.");
		}

		List<ObjectId> categoriesIds = filter.getSubCategories() == null
				? categoryService.getCategoriesIdsByParentId(filter.getParentCategoryId())
				: convertToObjectIdList(filter.getSubCategories());

		query.addCriteria(Criteria.where("subCategory").in(categoriesIds));

		List<String> productsSkuCodes;
		if (filter.getColors() != null) {
			productsSkuCodes = findByCategoryIdAndColorId(categoriesIds, filter.getColors()).stream()
					.map(Product::getSkuCode).toList();
			query.addCriteria(Criteria.where("_id").in(productsSkuCodes));
		}

		if (filter.getMaterials() != null) {
			List<ObjectId> materialsIds = convertToObjectIdList(filter.getMaterials());
			query.addCriteria(Criteria.where("materials").in(materialsIds));
		}

		if (filter.getCollections() != null) {
			List<ObjectId> collectionsIds = convertToObjectIdList(filter.getCollections());
			query.addCriteria(Criteria.where("collection").in(collectionsIds));
		}

		if (filter.getPriceMin() != null && filter.getPriceMax() != null) {
			query.addCriteria(Criteria.where("priceWithDiscount").lte(Decimal128.parse(filter.getPriceMax()))
					.gte(Decimal128.parse(filter.getPriceMin())));
		}

		if (filter.isSale()) {
			query.addCriteria(Criteria.where("discount").gt(0));
		}

		if (filter.getWeightMax() > 0 && filter.getWeightMin() > 0) {
			query.addCriteria(Criteria.where("weight").lte(filter.getWeightMax()).gte(filter.getWeightMin()));
		}

		if (filter.getHeightMax() > 0 && filter.getHeightMin() > 0) {
			query.addCriteria(Criteria.where("height").lte(filter.getHeightMax()).gte(filter.getHeightMin()));
		}

		if (filter.getWidthMax() > 0 && filter.getWidthMin() > 0) {
			query.addCriteria(Criteria.where("width").lte(filter.getWidthMax()).gte(filter.getWidthMin()));
		}

		if (filter.getDepthMax() > 0 && filter.getDepthMin() > 0) {
			query.addCriteria(Criteria.where("depth").lte(filter.getDepthMax()).gte(filter.getDepthMin()));
		}

		if (filter.isTransformation()) {
			query.addCriteria(Criteria.where("transformation").is(filter.isTransformation()));
		}

		if (filter.isHeightAdjustment()) {
			query.addCriteria(Criteria.where("heightAdjustment").is(filter.isHeightAdjustment()));
		}

		if (filter.getNumberOfDoorsMin() > 0 && filter.getNumberOfDoorsMax() > 0) {
			query.addCriteria(Criteria.where("numberOfDoors").lte(filter.getNumberOfDoorsMax())
					.gte(filter.getNumberOfDoorsMin()));
		}

		if (filter.getNumberOfDrawersMin() > 0 && filter.getNumberOfDrawersMax() > 0) {
			query.addCriteria(Criteria.where("numberOfDrawers").lte(filter.getNumberOfDrawersMax())
					.gte(filter.getNumberOfDrawersMin()));
		}

		if (filter.getBedLengthMin() > 0 && filter.getBedLengthMax() > 0) {
			query.addCriteria(Criteria.where("bedLength").lte(filter.getBedLengthMax()).gte(filter.getBedLengthMin()));
		}

		if (filter.getBedWidthMin() > 0 && filter.getBedWidthMax() > 0) {
			query.addCriteria(Criteria.where("bedWidth").lte(filter.getBedWidthMax()).gte(filter.getBedWidthMin()));
		}

		if (filter.getMaxLoad() != null) {
			MaxLoadEnum.MaxLoad maxLoad = MaxLoadEnum.findValues(filter.getMaxLoad());

			if (maxLoad.getLoadMin() > 0 && maxLoad.getLoadMax() > 0 && maxLoad.getLess() == 0
					&& maxLoad.getMore() == 0) {
				query.addCriteria(Criteria.where("maxLoad").lte(maxLoad.getLoadMax()).gte(maxLoad.getLoadMin()));
			} else if (maxLoad.getLoadMin() == 0 && maxLoad.getLoadMax() == 0 && maxLoad.getLess() > 0
					&& maxLoad.getMore() > 0) {
				query.addCriteria(Criteria.where("maxLoad").lte(maxLoad.getLess()).gte(maxLoad.getMore()));
			}
		}

		return mongoTemplate.find(query, Product.class);
	}

	private List<Product> findByCategoryIdAndColorId(List<ObjectId> categoriesIds,
			List<? extends ParametersDto> colorsId) {
		List<String> colorsIdsObjectId = convertColorToStringIdList(colorsId);
		LookupOperation lookup = LookupOperation.newLookup().from("ImageProduct").localField("_id")
				.foreignField("product.$id").as("imageProduct");

		MatchOperation matchCategoryId = Aggregation.match(new Criteria("subCategory.$id").in(categoriesIds));
		MatchOperation matchColorId = Aggregation.match(new Criteria("imageProduct.color.$id").in(colorsIdsObjectId));

		Aggregation aggregation = Aggregation.newAggregation(lookup, matchCategoryId, matchColorId);

		return mongoTemplate.aggregate(aggregation, Product.class, Product.class).getMappedResults();
	}

	private List<ObjectId> convertToObjectIdList(List<? extends ParametersDto> stringList) {
		return stringList.stream().map(ParametersDto::getId).map(ObjectId::new).toList();
	}

	private List<String> convertColorToStringIdList(List<? extends ParametersDto> stringList) {
		return stringList.stream().map(ParametersDto::getId).toList();
	}

	@Override
	public List<Product> search(String keyWord) {
		final Query query = new Query();
		final String pattern = ".*" + keyWord + ".*";
		final String caseInsensitive = "i";
		Criteria skuCodeCriteria = Criteria.where("skuCode").regex(pattern);		
	    Criteria nameCriteria = Criteria.where("name").regex(pattern, caseInsensitive);	

		query.addCriteria(new Criteria().orOperator(skuCodeCriteria, nameCriteria));
	    return mongoTemplate.find(query, Product.class);
	}
}
