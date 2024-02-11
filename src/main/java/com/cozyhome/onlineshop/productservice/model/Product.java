package com.cozyhome.onlineshop.productservice.model;

import com.cozyhome.onlineshop.productservice.model.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
@Document(collection = "Product")
public class Product {
    @Id
    private String skuCode;
    private String name;
    private String description;
    private String shortDescription;
    @Indexed
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal price;
    private byte discount;
    @Indexed
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal priceWithDiscount;
    @Indexed
    private ProductStatus status;
    @DBRef
    private Collection collection;
    @Indexed
    @DBRef
    private Category subCategory;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    @Indexed
    private float averageRating;
    @Indexed
    private byte popularRating;
    @Indexed
    @DBRef
    private List<Material> materials;

    private Boolean transformation;
    private Boolean heightRegulation;

    private Byte numberOfDoors;
    private Byte numberOfDrawers;

    private Float bedLength;
    private Float bedWidth;

    private short maxLoad;

    private Float weight;
    private Float height;
    private Float width;
    private Float depth;

    private boolean available;
}
