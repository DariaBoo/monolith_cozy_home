package com.cozyhome.onlineshop.productservice.repository.impl;

import com.cozyhome.onlineshop.dto.request.ProductColorDto;
import com.cozyhome.onlineshop.productservice.model.Color;
import com.cozyhome.onlineshop.productservice.model.ImageProduct;
import com.cozyhome.onlineshop.productservice.repository.ImageRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.lookup;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.replaceRoot;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

@RequiredArgsConstructor
@Repository
public class ImageRepositoryCustomImpl implements ImageRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<ImageProduct> findImagesByMainPhotoAndProductSkuCodeIn(List<String> skuCodes, boolean isMain) {
        Aggregation aggregation = Aggregation.newAggregation(
            match(Criteria.where("product.$id").in(skuCodes)),
            match(Criteria.where("mainPhoto").is(isMain)),
            group("product.$id").first("$$ROOT").as("firstImage"),
            replaceRoot("firstImage"));
        return mongoTemplate.aggregate(aggregation, ImageProduct.class, ImageProduct.class).getMappedResults();
    }

    @Override
    public List<Color> findColorsByProductSkuCodeIn(List<String> productSkuCodes) {

        Aggregation aggregation = Aggregation.newAggregation(match(Criteria.where("product.$id").in(productSkuCodes)),
            lookup("Color", "color.$id", "_id", "color"), unwind("color"), replaceRoot("color"),
            group("_id").first("$$ROOT").as("color"), replaceRoot("color"));

        return mongoTemplate.aggregate(aggregation, ImageProduct.class, Color.class).getMappedResults();
    }

    @Override
    public Map<String, List<Color>> groupColorsByProductSkuCodeIn(List<String> skuCodes) {
        Map<String, List<Color>> colorsMap = new HashMap<>();
        Aggregation aggregation = Aggregation.newAggregation(match(Criteria.where("product.$id").in(skuCodes)),
            group("product.$id").addToSet("color").as("colors"));
        AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, ImageProduct.class, Map.class);
        for (Map<String, List<Color>> entry : result.getMappedResults()) {
            List<Color> colors = entry.get("colors");
            String skuCode = String.valueOf(entry.get("_id"));
            colorsMap.put(skuCode, colors);
        }
        return colorsMap;
    }

    @Override
    public List<Color> findColorsByProductSkuCode(String productSkuCode) {
        Aggregation aggregation = Aggregation.newAggregation(match(Criteria.where("product.$id").is(productSkuCode)),
            lookup("Color", "color.$id", "_id", "color"), unwind("color"), replaceRoot("color"),
            group("_id").first("$$ROOT").as("color"), replaceRoot("color"));

        return mongoTemplate.aggregate(aggregation, ImageProduct.class, Color.class).getMappedResults();
    }

    @Override
    public Map<ProductColorDto, ImageProduct> findImagesByMainPhotoTrueAndProductSkuCodeWithColorHexIn(List<ProductColorDto> productColorDtos) {
        List<String> skuCodes = productColorDtos.stream().map(ProductColorDto::getProductSkuCode).toList();
        Map<ProductColorDto, ImageProduct> imagesMap = new HashMap<>();
        Map<String, List<ImageProduct>> resultMap = new HashMap<>();

        Aggregation aggregation = Aggregation.newAggregation(
            match(Criteria.where("product.$id").in(skuCodes).and("mainPhoto").is(true)),
            group("product.$id").addToSet("$$ROOT").as("image"));

        AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, ImageProduct.class, Map.class);
        for (Map<String, List<ImageProduct>> entry : result.getMappedResults()) {
            resultMap.put(String.valueOf(entry.get("_id")), entry.get("image"));
        }

        for (ProductColorDto entry : productColorDtos) {
            List<ImageProduct> images = resultMap.get(entry.getProductSkuCode());
            Optional<ImageProduct> image = images.stream().filter(imageProduct -> imageProduct.getColor().getId().equals(entry.getColorHex())).findFirst();

            if (image.isPresent()) {
                imagesMap.put(new ProductColorDto(entry.getProductSkuCode(), entry.getColorHex()), image.get());
            } else {
                imagesMap.put(new ProductColorDto(entry.getProductSkuCode(), entry.getColorHex()), null);
            }
        }
        return imagesMap;
    }

	@Override
	public Map<ProductColorDto, ImageProduct> findMainImagesByProductColorList(List<ProductColorDto> productColorDtos) {
		List<String> skuCodeList = extractValues(productColorDtos, ProductColorDto::getProductSkuCode);		
		List<String> colorHexList = extractValues(productColorDtos, ProductColorDto::getColorHex);		
		
		Aggregation aggregation = Aggregation.newAggregation(
			    match(Criteria.where("product.$id").in(skuCodeList)
			        .and("color.$id").in(colorHexList)
			        .and("mainPhoto").is(true))
			);		
		List<ImageProduct> results = mongoTemplate.aggregate(aggregation, ImageProduct.class, ImageProduct.class)
				.getMappedResults();				
	    
		return buildResultMap(results);
	}
	
	private <T> List<T> extractValues(List<ProductColorDto> list, Function<ProductColorDto, T> function){
		return list.stream().map(function).toList();
	}
	
	private Map<ProductColorDto, ImageProduct> buildResultMap(List<ImageProduct> imageProductList){
		return imageProductList.stream()
	            .collect(Collectors.toMap(imageProduct -> new ProductColorDto(imageProduct.getProduct().getSkuCode(), imageProduct.getColor().getId()), 
	            		imageProduct -> imageProduct));
	}
}
