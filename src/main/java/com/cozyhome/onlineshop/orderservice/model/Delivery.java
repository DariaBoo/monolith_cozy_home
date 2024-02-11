package com.cozyhome.onlineshop.orderservice.model;

import com.cozyhome.onlineshop.orderservice.model.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@SuperBuilder
@Document(collection = "Delivery")
public class Delivery {

    @Id
    private ObjectId id;
    private String city;
    private PaymentMethod paymentMethod;
    @EqualsAndHashCode.Exclude
    private LocalDateTime createdAt;
    @EqualsAndHashCode.Exclude
    private LocalDateTime modifiedAt;
}
