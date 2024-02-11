package com.cozyhome.onlineshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PopUpImageDto {
    private String id;
    private String desktopPopUpImagePath;
    private String mobilePopUpImagePath;
}
