package com.cozyhome.onlineshop.orderservice.model;

import com.cozyhome.onlineshop.orderservice.model.enums.OrderStatus;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "Order")
public class Order {
    @Id
    private ObjectId id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    @DBRef
    private Delivery delivery;
    @DBRef
    private List<OrderItem> orderItem;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String userId;
    private int orderNumber;
}
