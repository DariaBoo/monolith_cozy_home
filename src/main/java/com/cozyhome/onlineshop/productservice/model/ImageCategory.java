package com.cozyhome.onlineshop.productservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(collection = "ImageCategory")
public class ImageCategory {

    @Id
    private ObjectId id;
    @DBRef
    @Indexed
    private Category category;
    private boolean catalog;
    private String imageSize;
    private String categoryImageName;

    public enum ImageSize {
        SMALL("304x250"),
        LARGE("640x250");

        private final String size;

        ImageSize(String size) {
            this.size = size;
        }

        public String getSize() {
            return size;
        }
    }
}
