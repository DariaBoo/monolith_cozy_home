package com.cozyhome.onlineshop.dto.order;

import com.cozyhome.onlineshop.orderservice.model.enums.PaymentMethod;
import com.cozyhome.onlineshop.validation.ValidDeliveryOptions;
import com.cozyhome.onlineshop.validation.ValidEnum;
import com.cozyhome.onlineshop.validation.ValidName;
import com.cozyhome.onlineshop.validation.ValidOptionalComment;
import com.cozyhome.onlineshop.validation.ValidOptionalName;
import com.cozyhome.onlineshop.validation.ValidOptionalRegion;

import jakarta.validation.constraints.Size;

import com.cozyhome.onlineshop.validation.ValidOptionalAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ValidDeliveryOptions
public class DeliveryDto {
    @ValidName
    private String city;
    @ValidEnum(enumClass = PaymentMethod.class)
    private String paymentMethod;

    @ValidOptionalAddress
    private String street;
    @Size(min = 0, max = 30)
    private String house;
    @Size(min = 0, max = 30)
    private String apartment;
    @ValidOptionalComment
    private String comment;

    @ValidOptionalName
    private String deliveryCompanyName;
    @ValidOptionalRegion
    private String region;
    @ValidOptionalAddress
    private String postOffice;
}
