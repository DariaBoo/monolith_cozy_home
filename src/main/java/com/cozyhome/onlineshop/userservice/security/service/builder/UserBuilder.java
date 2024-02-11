package com.cozyhome.onlineshop.userservice.security.service.builder;

import com.cozyhome.onlineshop.dto.user.UserInformationResponse;
import com.cozyhome.onlineshop.userservice.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserBuilder {

    public UserInformationResponse buildUserInformationResponse(User user) {
        UserInformationResponse userInformationResponse = UserInformationResponse.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .build();
        if (user.getBirthday() != null && !user.getBirthday().toString().isEmpty()) {
            userInformationResponse.setBirthday(user.getBirthday());
        }
        return userInformationResponse;
    }
}
