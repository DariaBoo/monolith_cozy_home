package com.cozyhome.onlineshop.dto.productcard;

import com.cozyhome.onlineshop.dto.CollectionDto;
import com.cozyhome.onlineshop.dto.review.ReviewResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductCardDto {
    private String categoryName;
    private String subCategoryName;

    private String skuCode;
    private String name;
    private String description;
    private BigDecimal price;
    private byte discount;
    private BigDecimal priceWithDiscount;
    private List<ColorQuantityStatusDto> colors;

    private float averageRating;

    private byte countOfReviews;
    private List<ReviewResponse> reviews;

    private List<ProductCardImageDto> images;

    private List<String> materials;
    private CollectionDto collection;
    private boolean transformation;
    private boolean heightAdjustment;
    private float weight;
    private float height;
    private float width;
    private float depth;
    private byte numberOfDoors;
    private byte numberOfDrawers;
    private float bedLength;
    private float bedWidth;
    private short maxLoad;

    private String quantityStatus;
    private boolean isFavorite;
    
}
