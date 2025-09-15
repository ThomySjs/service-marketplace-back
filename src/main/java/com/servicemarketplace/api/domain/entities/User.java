package com.servicemarketplace.api.domain.entities;

import java.time.LocalDateTime;

import com.servicemarketplace.api.config.Roles;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private boolean verified = false;
    @Builder.Default
    private String role = Roles.USER.name();
    private LocalDateTime createdAt;
    private Long zoneId;
    @Builder.Default
    private boolean deleted = false;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public boolean isAdmin() {
        return this.role.equals(Roles.ADMIN.name());
    }

}
