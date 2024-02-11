package com.cozyhome.onlineshop.dto.request;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SortDto {

    @Pattern(regexp = "^[-_$A-a-Z-z]{1,30}$", message = "Invalid field name. It's optional parameter. " +
        "Must include 1 to 30 letters and it's allowed '-', '_' and '$'.")
    private String fieldName;

    @Pattern(regexp = "(asc|desc)", message = "Invalid direction. It's optional parameter. Must be 'asc' or 'desc'.")
    private String direction;
}
