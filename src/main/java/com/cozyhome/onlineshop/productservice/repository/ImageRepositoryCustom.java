package com.cozyhome.onlineshop.productservice.repository;

import com.cozyhome.onlineshop.dto.request.ProductColorDto;
import com.cozyhome.onlineshop.productservice.model.Color;
import com.cozyhome.onlineshop.productservice.model.ImageProduct;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ImageRepositoryCustom {

	List<ImageProduct> findImagesByMainPhotoAndProductSkuCodeIn(List<String> skuCodes, boolean isMain);

    List<Color> findColorsByProductSkuCodeIn(List<String> productSkuCodes);

    List<Color> findColorsByProductSkuCode(String productSkuCode);

    Map<String, List<Color>> groupColorsByProductSkuCodeIn(List<String> productSkuCodes);

//    Map<ProductColorDto, ImageProduct> findImagesByMainPhotoTrueAndProductSkuCodeWithColorHexIn(List<ProductColorDto> productColorDtos);

    Map<ProductColorDto, ImageProduct> findMainImagesByProductColorList(List<ProductColorDto> productColorDtos);

    Map<String, ImageProduct> findMainFirstImagesBySkuCodeIn(List<String> skuCodes);
}
