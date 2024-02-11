package com.cozyhome.onlineshop.dto.filter;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
public class ParameterDetailsDto extends ParametersDto {
    private String name;
    private int countOfProducts;

    public ParameterDetailsDto(String id, String name, int countOfProducts) {
        super(id);
        this.name = name;
        this.countOfProducts = countOfProducts;
    }

    public ParameterDetailsDto(String id, String name) {
        super(id);
        this.name = name;
    }
}
