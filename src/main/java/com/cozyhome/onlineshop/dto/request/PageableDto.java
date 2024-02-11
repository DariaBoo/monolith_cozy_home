package com.cozyhome.onlineshop.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PageableDto {

    @Min(value = 0)
    @Max(value = 30)
    private int page;

    @Min(value = 1)
    @Max(value = 30)
    private int size;
}
