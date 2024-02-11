package com.cozyhome.onlineshop.orderservice.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "OrderItem")
public class OrderItem {
    @Id
    private ObjectId id;
    @Indexed
    private int productColorId;
    private short quantity;
    @Indexed
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal price;
}
