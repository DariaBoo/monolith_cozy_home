package com.cozyhome.onlineshop.productservice.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
@Document(collection = "ImageProduct")
public class ImageProduct {

    @Id
    private ObjectId id;
    @DBRef
    private Color color;
    @DBRef
    @Indexed
    private Product product;
    private boolean mainPhoto;
    private String popUpImageName;
    private String desktopImageName;
    private String sliderImageName;
    private String mobileImageName;
    private String previewImageName;
}
