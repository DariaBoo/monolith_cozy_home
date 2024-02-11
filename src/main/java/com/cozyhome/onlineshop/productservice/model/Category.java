package com.cozyhome.onlineshop.productservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(collection = "Category")
public class Category {

    @Id
    private ObjectId id;
    @UniqueElements
    private String name;
    private boolean active;
    @Indexed
    private ObjectId parentId;
    private String spriteIcon;
}
