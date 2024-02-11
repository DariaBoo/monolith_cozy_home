package com.cozyhome.onlineshop.dto.filter;

import com.cozyhome.onlineshop.validation.ValidId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FilterDto {
    private short countOfProducts;
    private byte countOfPages;

    private String keyWord;
    
    @ValidId
    private String parentCategoryId;
    private List<? extends ParametersDto> subCategories;

    private List<? extends ParametersDto> colors;
    private List<? extends ParametersDto> materials;
    private List<? extends ParametersDto> collections;

    private boolean sale;
    private boolean transformation;
    private boolean heightAdjustment;

    private String priceMin;
    private String priceMax;

    private float weightMin;
    private float weightMax;

    private float heightMin;
    private float heightMax;

    private float widthMin;
    private float widthMax;

    private float depthMin;
    private float depthMax;

    private byte numberOfDoorsMin;
    private byte numberOfDoorsMax;

    private byte numberOfDrawersMin;
    private byte numberOfDrawersMax;

    private float bedLengthMin;
    private float bedLengthMax;

    private float bedWidthMin;
    private float bedWidthMax;

    private List<String> maxLoad;
}
