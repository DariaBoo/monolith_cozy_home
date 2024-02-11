package com.cozyhome.onlineshop.orderservice.service;

import com.cozyhome.onlineshop.dto.order.DeliveryCompanyAdminDto;
import com.cozyhome.onlineshop.dto.order.DeliveryCompanyDto;

import java.util.List;

public interface DeliveryService {
    List<DeliveryCompanyDto> getDeliveryCompanies();

    DeliveryCompanyAdminDto saveNewDeliveryCompany(String companyName);

    DeliveryCompanyAdminDto deleteDeliveryCompany(String companyName);
}
