package com.cozyhome.onlineshop.productservice.controller;

import com.cozyhome.onlineshop.dto.MaterialDto;
import com.cozyhome.onlineshop.productservice.controller.swagger.CommonApiResponses;
import com.cozyhome.onlineshop.productservice.controller.swagger.SwaggerResponse;
import com.cozyhome.onlineshop.productservice.service.MaterialService;
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
        "${api.front.additional_url}", "${api.front.main.url}", "${api.front.temporal.url}" })
@Tag(name = "Material")
@CommonApiResponses
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.basePath}/material")
public class MaterialController {
    private final MaterialService materialService;

    @Operation(summary = "Fetch all materials", description = "Fetch all materials with id and name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
    @GetMapping
    public ResponseEntity<List<MaterialDto>> getMaterials() {
        return new ResponseEntity<>(materialService.getMaterials(), HttpStatus.OK);
    }
}
