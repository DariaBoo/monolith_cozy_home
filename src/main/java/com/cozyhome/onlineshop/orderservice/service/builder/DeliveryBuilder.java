package com.cozyhome.onlineshop.orderservice.service.builder;

import com.cozyhome.onlineshop.dto.order.DeliveryDto;
import com.cozyhome.onlineshop.exception.DataNotFoundException;
import com.cozyhome.onlineshop.orderservice.model.AddressDelivery;
import com.cozyhome.onlineshop.orderservice.model.Delivery;
import com.cozyhome.onlineshop.orderservice.model.DeliveryCompany;
import com.cozyhome.onlineshop.orderservice.model.PostalDelivery;
import com.cozyhome.onlineshop.orderservice.model.enums.EntityStatus;
import com.cozyhome.onlineshop.orderservice.model.enums.PaymentMethod;
import com.cozyhome.onlineshop.orderservice.repository.DeliveryCompanyRepository;
import com.cozyhome.onlineshop.orderservice.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryBuilder {
	
    private final DeliveryCompanyRepository deliveryCompanyRepository;
    private final DeliveryRepository deliveryRepository;

    public Delivery buildPostalDelivery(DeliveryDto deliveryDto) {
        DeliveryCompany deliveryCompany = deliveryCompanyRepository.findByNameAndStatus(deliveryDto.getDeliveryCompanyName(),
                EntityStatus.ACTIVE)
                .orElseThrow(() -> new DataNotFoundException(String.format("Delivery company %s doesn't found", deliveryDto.getDeliveryCompanyName())));

        Delivery delivery = PostalDelivery.builder()
                .city(deliveryDto.getCity())
                .paymentMethod(PaymentMethod.valueOf(deliveryDto.getPaymentMethod().toUpperCase()))
                .createdAt(LocalDateTime.now())
                .deliveryCompany(deliveryCompany)
                .region(deliveryDto.getRegion())
                .postOffice(deliveryDto.getPostOffice())
                .build();

        return deliveryRepository.save(delivery);
    }

    public Delivery buildAddressDelivery(DeliveryDto deliveryDto) {
        String comment;
        if (deliveryDto.getComment() == null || deliveryDto.getComment().isEmpty()) {
            comment = "";
        } else {
            comment = deliveryDto.getComment();
        }

        Delivery delivery = AddressDelivery.builder()
                .city(deliveryDto.getCity())
                .paymentMethod(PaymentMethod.valueOf(deliveryDto.getPaymentMethod().toUpperCase()))
                .createdAt(LocalDateTime.now())
                .street(deliveryDto.getStreet())
                .house(deliveryDto.getHouse())
                .apartment(deliveryDto.getApartment())
                .comment(comment)
                .build();

        return deliveryRepository.save(delivery);
    }
}
