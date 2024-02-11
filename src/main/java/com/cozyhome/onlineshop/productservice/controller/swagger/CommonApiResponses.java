package com.cozyhome.onlineshop.productservice.controller.swagger;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
    @ApiResponse(responseCode = SwaggerResponse.Code.CODE_401, description = SwaggerResponse.Message.CODE_401),
    @ApiResponse(responseCode = SwaggerResponse.Code.CODE_500, description = SwaggerResponse.Message.CODE_500)
})
public @interface CommonApiResponses {

}
