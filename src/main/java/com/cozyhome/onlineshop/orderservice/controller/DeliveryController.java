package com.cozyhome.onlineshop.orderservice.controller;

import com.cozyhome.onlineshop.dto.order.DeliveryCompanyAdminDto;
import com.cozyhome.onlineshop.dto.order.DeliveryCompanyDto;
import com.cozyhome.onlineshop.orderservice.service.DeliveryService;
import com.cozyhome.onlineshop.productservice.controller.swagger.CommonApiResponses;
import com.cozyhome.onlineshop.productservice.controller.swagger.SwaggerResponse;
import com.cozyhome.onlineshop.validation.ValidName;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin({ "${api.front.base_url}", "${api.front.localhost}", "${api.front.test_url}",
        "${api.front.additional_url}", "${api.front.main.url}" })
@Tag(name = "Delivery")
@CommonApiResponses
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("${api.basePath}/delivery")
public class DeliveryController {
    private final DeliveryService deliveryService;

    @Operation(summary = "Fetch all names of delivery companies", description = "Fetch all names of delivery companies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
    @GetMapping
    public ResponseEntity<List<DeliveryCompanyDto>> getDeliveryCompanies() {
        return new ResponseEntity<>(deliveryService.getDeliveryCompanies(), HttpStatus.OK);
    }

    @Operation(summary = "Save new delivery company", description = "Save new delivery company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
    @PostMapping
    public ResponseEntity<DeliveryCompanyAdminDto> saveNewDeliveryCompany(@RequestParam @ValidName String companyName) {
        return new ResponseEntity<>(deliveryService.saveNewDeliveryCompany(companyName), HttpStatus.OK);
    }

    @Operation(summary = "Save new delivery company", description = "Save new delivery company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
    @DeleteMapping
    public ResponseEntity<DeliveryCompanyAdminDto> deleteDeliveryCompany(@RequestParam @ValidName String companyName) {
        return new ResponseEntity<>(deliveryService.deleteDeliveryCompany(companyName), HttpStatus.OK);
    }
}
