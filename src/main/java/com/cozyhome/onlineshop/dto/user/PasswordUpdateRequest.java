package com.cozyhome.onlineshop.dto.user;

import com.cozyhome.onlineshop.validation.ValidNewPasswordsMatch;
import com.cozyhome.onlineshop.validation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ValidNewPasswordsMatch(
        field = "newPassword",
        fieldMatch = "repeatedNewPassword",
        message = "Fields new password and password reset don't match."
)
public class PasswordUpdateRequest {

    @ValidPassword
    private String oldPassword;
    @ValidPassword
    private String newPassword;
    @ValidPassword
    private String repeatedNewPassword;
}
