package com.cozyhome.onlineshop.orderservice.controller;

import com.cozyhome.onlineshop.dto.order.OrderDto;
import com.cozyhome.onlineshop.dto.order.OrderNumberDto;
import com.cozyhome.onlineshop.orderservice.service.OrderService;
import com.cozyhome.onlineshop.productservice.controller.swagger.CommonApiResponses;
import com.cozyhome.onlineshop.productservice.controller.swagger.SwaggerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin({ "${api.front.base_url}", "${api.front.localhost}", "${api.front.test_url}",
        "${api.front.additional_url}", "${api.front.main.url}", "${api.front.test2.url}" })
@Tag(name = "Order")
@CommonApiResponses
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.basePath}/order")
public class OrderController {

    private final OrderService orderService;
    @Value("${header.name.user-id}")
    private String userIdAttribute;

    @Operation(summary = "Create new order", description = "Create new order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
    @PostMapping
    public ResponseEntity<OrderNumberDto> createOrder(@RequestBody @Valid OrderDto orderDto, HttpServletRequest request) {
        String userId = (String) request.getAttribute(userIdAttribute);
        return new ResponseEntity<>(orderService.createOrder(orderDto, userId), HttpStatus.OK);
    }
    
    @Operation(summary = "Delete all orders by email", description = "Delete all orders by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_DELETED_DESCRIPTION) })
    @DeleteMapping
    public ResponseEntity<Void> deleteOrderByEmail(@RequestParam String email){
    	orderService.deleteByEmail(email);
    	return ResponseEntity.ok().build();
    }
}
