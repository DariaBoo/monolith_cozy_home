package com.cozyhome.onlineshop.productservice.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum ProductStatus {
    NEW((byte) 0),
    POPULAR((byte) 1),
    REGULAR((byte) 2),
    DELETED((byte) 3);

    private final Byte description;

    private static final Map<Byte, ProductStatus> PRODUCT_STATUS_MAP = new HashMap<>();

    static {
        for (ProductStatus element : values()) {
            PRODUCT_STATUS_MAP.put(element.description, element);
        }
    }

    public static ProductStatus valueOfDescription(Byte description) {
        return PRODUCT_STATUS_MAP.get(description);
    }

}
