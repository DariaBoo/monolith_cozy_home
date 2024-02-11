package com.cozyhome.onlineshop.dto.review;

import com.cozyhome.onlineshop.userservice.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReviewToRemoveDto {
    private String reviewId;
    private String userId;
    private Set<Role> roles;
}