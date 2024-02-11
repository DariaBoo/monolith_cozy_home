package com.cozyhome.onlineshop.orderservice.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class PostalDelivery extends Delivery{
    @DBRef
    private DeliveryCompany deliveryCompany;
    private String region;
    private String postOffice;
}
