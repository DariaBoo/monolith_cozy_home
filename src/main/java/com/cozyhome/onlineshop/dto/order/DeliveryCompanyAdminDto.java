package com.cozyhome.onlineshop.dto.order;

import com.cozyhome.onlineshop.orderservice.model.enums.EntityStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DeliveryCompanyAdminDto {
    private String id;
    private String name;
    private EntityStatus status;

}
