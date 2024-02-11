package com.cozyhome.onlineshop.dto.order;

import com.cozyhome.onlineshop.validation.ValidName;
import com.cozyhome.onlineshop.validation.ValidOptionalEmail;
import com.cozyhome.onlineshop.validation.ValidPhoneNumber;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDto {
    @ValidName
    private String firstName;
    @ValidName
    private String lastName;
    @ValidPhoneNumber
    private String phoneNumber;
    @ValidOptionalEmail
    private String email;
    @Valid
    private DeliveryDto delivery;
    @Valid
    private List<OrderItemDto> orderItems;
}
