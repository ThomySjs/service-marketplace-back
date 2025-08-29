package com.servicemarketplace.api.domain.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

enum Role {
    USER,
    ADMIN
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="users")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique=true)
    private String email;
    private String phone;
    private String password;
    private String address;
    @Builder.Default
    private final boolean verified = false;
    @Builder.Default
    private final String role = Role.USER.name();
    private Date createdAt;
    private Long zoneId;
    private String token;
    @Builder.Default
    private final boolean deleted = false;

}
