package com.cozyhome.onlineshop.dto.auth;

import com.cozyhome.onlineshop.validation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NewPasswordRequest {
    @ValidPassword
    private String password;
}
