package com.cozyhome.onlineshop.dto.review;

import com.cozyhome.onlineshop.validation.ValidEmail;
import com.cozyhome.onlineshop.validation.ValidName;
import com.cozyhome.onlineshop.validation.ValidSkuCode;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReviewRequest {
    @Max(value = 5, message = "value must be between 1 and 5.")
    @Min(value = 1, message = "value must be between 1 and 5.")
    private byte rating;
    @Size(max = 500, message = "comment must be to 500 characters.")
    private String comment;
    @ValidSkuCode
    private String productSkuCode;
    @ValidName
    private String userName;
    @ValidEmail
    private String email;
}
