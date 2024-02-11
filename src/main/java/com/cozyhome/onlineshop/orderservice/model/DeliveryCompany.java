package com.cozyhome.onlineshop.orderservice.model;

import com.cozyhome.onlineshop.orderservice.model.enums.EntityStatus;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "PostService")
public class DeliveryCompany {
    @Id
    private ObjectId id;
    private String name;
    private EntityStatus status;
}
