package com.cozyhome.onlineshop.productservice.controller;

import com.cozyhome.onlineshop.dto.ColorDto;
import com.cozyhome.onlineshop.productservice.controller.swagger.CommonApiResponses;
import com.cozyhome.onlineshop.productservice.controller.swagger.SwaggerResponse;
import com.cozyhome.onlineshop.productservice.service.ColorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin({ "${api.front.base_url}", "${api.front.localhost}", "${api.front.test_url}",
        "${api.front.additional_url}", "${api.front.main.url}", "${api.front.temporal.url}", "${api.front.test2.url}" })
@Tag(name = "Color")
@CommonApiResponses
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.basePath}/colors")
public class ColorController {
    private final ColorService colorService;

    @Operation(summary = "Fetch all colors", description = "Fetch all colors with id and name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
    @GetMapping
    public ResponseEntity<List<ColorDto>> getColors() {
        return new ResponseEntity<>(colorService.getColors(), HttpStatus.OK);
    }
}
