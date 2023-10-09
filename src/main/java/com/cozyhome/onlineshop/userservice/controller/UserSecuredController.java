package com.cozyhome.onlineshop.userservice.controller;

import com.cozyhome.onlineshop.dto.CategoryWithPhotoDto;
import com.cozyhome.onlineshop.dto.user.UserInformationDto;
import com.cozyhome.onlineshop.productservice.controller.swagger.SwaggerResponse;
import com.cozyhome.onlineshop.userservice.security.service.UserService;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin({ "${api.front.base_url}", "${api.front.localhost}", "${api.front.test_url}",
        "${api.front.additional_url}", "${api.front.main.url}" })
@Tag(name = "User")
@ApiResponse
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("${api.secure.basePath}/user")
public class UserSecuredController {
    @Value("${header.name.user-id}")
    private String userIdName;
    private final UserService userService;

    @Operation(summary = "change of user information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerResponse.Code.CODE_200, description = SwaggerResponse.Message.CODE_200_FOUND_DESCRIPTION) })
    @Secured({"ROLE_CUSTOMER"})
    @GetMapping("/update")
    public ResponseEntity<Void> updateUserData(@Valid UserInformationDto userInformationDto,
                                               HttpServletRequest request) {
        String userId = (String) request.getAttribute(userIdName);
        userService.updateUserData(userInformationDto, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
