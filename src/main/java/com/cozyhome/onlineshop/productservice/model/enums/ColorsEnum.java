package com.cozyhome.onlineshop.productservice.model.enums;

public enum ColorsEnum {
	Сірий("#545454"),
	Чорний("#262626"),
	Коричневий("#C57100");
	
	private String colorHex;
	
	private ColorsEnum(String colorName) {
		this.colorHex = colorName;
	}

	public String getColorHex() {
        return colorHex;
    }

    public static String getColorNameByHex(String colorHex) {
        for (ColorsEnum colorEnum : ColorsEnum.values()) {
            if (colorEnum.colorHex.equalsIgnoreCase(colorHex)) {
                return colorEnum.name();
            }
        }
        return ""; 
    }
}
