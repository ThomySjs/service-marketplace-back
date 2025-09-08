package com.servicemarketplace.api.mappers;

import com.servicemarketplace.api.domain.entities.User;
import com.servicemarketplace.api.dto.UserDTO;

public class UserMapper {

    /**
     * Mapea el usuario a un DTO con los datos no sensibles.
     */
    public static UserDTO toUserDTO(User user) {
        return UserDTO.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .address(user.getAddress())
            .phone(user.getPhone())
            .createdAt(user.getCreatedAt())
            .build();
    }
}
