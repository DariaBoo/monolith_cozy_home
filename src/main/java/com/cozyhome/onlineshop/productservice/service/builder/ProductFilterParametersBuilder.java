package com.cozyhome.onlineshop.productservice.service.builder;

import com.cozyhome.onlineshop.dto.filter.FilterDto;
import com.cozyhome.onlineshop.dto.filter.ParameterDetailsDto;
import com.cozyhome.onlineshop.productservice.model.Color;
import com.cozyhome.onlineshop.productservice.model.Material;
import com.cozyhome.onlineshop.productservice.model.Product;
import com.cozyhome.onlineshop.productservice.model.enums.MaxLoadEnum;
import com.cozyhome.onlineshop.productservice.repository.ImageRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductFilterParametersBuilder {
    private final ImageRepositoryCustom imageRepositoryCustom;

    @Value("${digits.after.decimal}")
    private int digitsAfterDecimal;
    
    public FilterDto buildFilterParameters(List<Product> filteredProducts, List<Product> notFilteredProducts, byte size) {
        List<String> productsSkuCodes = filteredProducts.stream().map(Product::getSkuCode).toList();
        short countOfProducts = (short) filteredProducts.size();
        byte countOfPages = (byte) ((countOfProducts + size - 1) / size);

        FilterDto filterDto = FilterDto.builder()
            .countOfProducts(countOfProducts)
            .countOfPages(countOfPages)
            .subCategories(filteredProducts.stream().map(product -> new ParameterDetailsDto(product.getSubCategory().getId().toString(),
                product.getSubCategory().getName())).distinct().toList())
            .colors(getColors(productsSkuCodes))
            .materials(getMaterials(filteredProducts))
            .collections(filteredProducts.stream().map(product -> new ParameterDetailsDto(product.getCollection().getId().toString(),
                product.getCollection().getName())).distinct().toList())
            .sale(filteredProducts.stream().anyMatch(product -> product.getDiscount() != 0))
            .transformation(filteredProducts.stream().filter(product -> product.getTransformation() != null)
                .anyMatch(Product::getTransformation))
            .heightAdjustment(filteredProducts.stream().filter(product -> product.getTransformation() != null)
                .anyMatch(Product::getHeightRegulation))
            .build();

        if (filteredProducts.stream().anyMatch(x -> x.getMaxLoad() != 0)) {
            filterDto.setMaxLoad(MaxLoadEnum.findDescription(getMinShortValue(filteredProducts), getMaxShortValue(filteredProducts)));
        }

        return buildRankedParameters(filterDto, notFilteredProducts);
    }
    private List<ParameterDetailsDto> getColors(List<String> productsSkuCodes) {
        List<Color> colors = imageRepositoryCustom.findColorsByProductSkuCodeIn(productsSkuCodes);
        if (colors.isEmpty()) {
            return new ArrayList<>();
        }
        return colors.stream()
            .map(color -> new ParameterDetailsDto(color.getId(), color.getName())).toList();
    }

    private List<ParameterDetailsDto> getMaterials(List<Product> products) {
        List<ParameterDetailsDto> parameterDetails = new ArrayList<>();
        Map<Material, Long> materialsMap = products.stream().flatMap(product -> product.getMaterials().stream())
            .collect(Collectors.groupingBy(material -> material, Collectors.counting()));

        for (Map.Entry<Material, Long> entry : materialsMap.entrySet()) {
            parameterDetails.add(new ParameterDetailsDto(entry.getKey().getId().toString(),
                entry.getKey().getName(), Math.toIntExact(entry.getValue())));
        }
        return parameterDetails;
    }

    private FilterDto buildRankedParameters(FilterDto filterDto, List<Product> notFilteredProducts) {
        if (notFilteredProducts.stream().anyMatch(x -> x.getPrice() != null)) {
            filterDto.setPriceMax(getMaxPrice(notFilteredProducts));
            filterDto.setPriceMin(getMinPrice(notFilteredProducts));
        }

        if (notFilteredProducts.stream().anyMatch(x -> x.getWeight() != null)) {
            List<Float> weightValues = notFilteredProducts.stream().filter(product -> product.getWidth() != null).map(Product::getWidth).toList();

            filterDto.setWeightMax(getMaxFloatValue(weightValues));
            filterDto.setWeightMin(getMinFloatValue(weightValues));
        }

        if (notFilteredProducts.stream().anyMatch(product -> product.getHeight() != null)) {
            List<Float> heightValues = notFilteredProducts.stream().filter(product -> product.getHeight() != null).map(Product::getHeight).toList();

            filterDto.setHeightMax(getMaxFloatValue(heightValues));
            filterDto.setHeightMin(getMinFloatValue(heightValues));
        }

        if (notFilteredProducts.stream().anyMatch(x -> x.getWidth() != null)) {
            List<Float> widthValues = notFilteredProducts.stream().filter(product -> product.getWidth() != null).map(Product::getWidth).toList();

            filterDto.setWidthMax(getMaxFloatValue(widthValues));
            filterDto.setWidthMax(getMinFloatValue(widthValues));
        }

        if (notFilteredProducts.stream().anyMatch(x -> x.getDepth() != null)) {
            List<Float> depthValues = notFilteredProducts.stream().filter(product -> product.getDepth() != null).map(Product::getDepth).toList();

            filterDto.setDepthMax(getMaxFloatValue(depthValues));
            filterDto.setDepthMin(getMinFloatValue(depthValues));
        }

        if (notFilteredProducts.stream().anyMatch(x -> x.getNumberOfDoors() != null)) {
            List<Byte> numberOfDoorsValues = notFilteredProducts.stream().filter(product -> product.getNumberOfDoors() != null)
                .map(Product::getNumberOfDoors).toList();

            filterDto.setNumberOfDoorsMax(getMaxByteValue(numberOfDoorsValues));
            filterDto.setNumberOfDoorsMin(getMinByteValue(numberOfDoorsValues));
        }

        if (notFilteredProducts.stream().anyMatch(x -> x.getNumberOfDrawers() != null)) {
            List<Byte> numberOfDrawersValues = notFilteredProducts.stream().filter(product -> product.getNumberOfDrawers() != null)
                .map(Product::getNumberOfDrawers).toList();

            filterDto.setNumberOfDrawersMax(getMaxByteValue(numberOfDrawersValues));
            filterDto.setNumberOfDrawersMin(getMinByteValue(numberOfDrawersValues));
        }

        if (notFilteredProducts.stream().anyMatch(x -> x.getBedLength() != null)) {
            List<Float> bedLengthValues = notFilteredProducts.stream().filter(product -> product.getBedLength() != null).map(Product::getBedLength).toList();

            filterDto.setBedLengthMax(getMaxFloatValue(bedLengthValues));
            filterDto.setBedLengthMin(getMinFloatValue(bedLengthValues));
        }

        if (notFilteredProducts.stream().anyMatch(x -> x.getBedWidth() != null)) {
            List<Float> bedWidthValues = notFilteredProducts.stream().filter(product -> product.getBedWidth() != null).map(Product::getBedWidth).toList();

            filterDto.setBedWidthMax(getMaxFloatValue(bedWidthValues));
            filterDto.setBedWidthMin(getMinFloatValue(bedWidthValues));
        }
        return filterDto;
    }

    private String getMaxPrice(List<Product> products) {
        return products.stream()
            .map(Product::getPriceWithDiscount)
            .filter(Objects::nonNull)
            .max(Comparator.naturalOrder())
            .map(price -> price.setScale(digitsAfterDecimal, RoundingMode.HALF_UP).toString()).get();
    }

    private String getMinPrice(List<Product> products) {
        return products.stream()
            .map(Product::getPriceWithDiscount)
            .filter(Objects::nonNull)
            .min(Comparator.naturalOrder())
            .map(price -> price.setScale(digitsAfterDecimal, RoundingMode.HALF_UP).toString()).get();
    }

    private Float getMaxFloatValue(List<Float> floatValues) {
        return floatValues.stream().max(Comparator.naturalOrder()).map(floatValue -> Float.valueOf(String.format("%.0f", floatValue))).get();
    }

    private Float getMinFloatValue(List<Float> floatValues) {
        return floatValues.stream().min(Comparator.naturalOrder()).map(floatValue -> Float.valueOf(String.format("%.0f", floatValue))).get();
    }

    private Byte getMaxByteValue(List<Byte> byteValues) {
        return byteValues.stream().max(Comparator.naturalOrder()).get();
    }

    private Byte getMinByteValue(List<Byte> byteValues) {
        return byteValues.stream().min(Comparator.naturalOrder()).get();
    }

    private Short getMaxShortValue(List<Product> products) {
        return products.stream().filter(product -> product.getMaxLoad() != 0).map(Product::getMaxLoad)
            .max(Comparator.naturalOrder()).get();
    }

    private Short getMinShortValue(List<Product> products) {
        return products.stream().filter(product -> product.getMaxLoad() != 0).map(Product::getMaxLoad)
            .min(Comparator.naturalOrder()).get();
    }
}
