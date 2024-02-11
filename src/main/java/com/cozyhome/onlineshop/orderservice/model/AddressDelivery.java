package com.cozyhome.onlineshop.orderservice.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class AddressDelivery extends Delivery {
    private String street;
    private String house;
    private String apartment;
    private String comment;
}
