package com.cozyhome.onlineshop.dto.user;

import com.cozyhome.onlineshop.validation.ValidName;
import com.cozyhome.onlineshop.validation.ValidOptionalBirthday;
import com.cozyhome.onlineshop.validation.ValidEmail;
import com.cozyhome.onlineshop.validation.ValidPhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserInformationRequest {
    @ValidEmail
    private String email;
    @ValidOptionalBirthday
    private String birthday;
    @ValidName
    private String firstName;
    @ValidName
    private String lastName;
    @ValidPhoneNumber
    private String phoneNumber;
}
