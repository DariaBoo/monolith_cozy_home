package com.cozyhome.onlineshop.orderservice.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.cozyhome.onlineshop.dto.order.DeliveryCompanyAdminDto;
import com.cozyhome.onlineshop.dto.order.DeliveryCompanyDto;
import com.cozyhome.onlineshop.exception.DataAlreadyExistException;
import com.cozyhome.onlineshop.exception.DataNotFoundException;
import com.cozyhome.onlineshop.orderservice.model.DeliveryCompany;
import com.cozyhome.onlineshop.orderservice.model.enums.EntityStatus;
import com.cozyhome.onlineshop.orderservice.repository.DeliveryCompanyRepository;
import com.cozyhome.onlineshop.orderservice.service.DeliveryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryCompanyRepository deliveryCompanyRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<DeliveryCompanyDto> getDeliveryCompanies() {
        List<DeliveryCompany> deliveryCompanies = deliveryCompanyRepository.findAllByStatus(EntityStatus.ACTIVE);
        if (deliveryCompanies.isEmpty()) {
            return new ArrayList<>();
        }
        return deliveryCompanies.stream().map(company -> modelMapper.map(company, DeliveryCompanyDto.class)).toList();
    }

    @Override
    public DeliveryCompanyAdminDto saveNewDeliveryCompany(String companyName) {
        boolean existCompany = deliveryCompanyRepository.existsByNameAndStatus(companyName, EntityStatus.ACTIVE);
        if (existCompany) {
            throw new DataAlreadyExistException("Company " + companyName + " already exist. You can't add it.");
        }

        DeliveryCompany deliveryCompany = DeliveryCompany.builder()
                .name(companyName)
                .status(EntityStatus.ACTIVE)
                .build();

        return modelMapper.map(deliveryCompanyRepository.save(deliveryCompany), DeliveryCompanyAdminDto.class);
    }

    @Override
    public DeliveryCompanyAdminDto deleteDeliveryCompany(String companyName) {
        DeliveryCompany deliveryCompany = deliveryCompanyRepository.findByNameAndStatus(companyName, EntityStatus.ACTIVE)
                .orElseThrow(() -> new DataNotFoundException("Company " + companyName + " doesn't exist."));
        deliveryCompany.setStatus(EntityStatus.DELETED);
        return modelMapper.map(deliveryCompanyRepository.save(deliveryCompany), DeliveryCompanyAdminDto.class);
    }
}
