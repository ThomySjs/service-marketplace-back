package com.servicemarketplace.api.domain.entities;

import java.time.LocalDateTime;
import java.util.List;

import com.servicemarketplace.api.config.Roles;
import com.servicemarketplace.api.dto.user.UpdateUserDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
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
    private String imagePath;
    @Builder.Default
    private boolean verified = false;
    @Builder.Default
    private String role = Roles.USER.name();
    @Column(columnDefinition = "TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6)")
    private LocalDateTime createdAt;
    @Column(nullable = true)
    private Long zoneId;
    @OneToMany(mappedBy = "seller", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Service> services;
    @Builder.Default
    private boolean deleted = false;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public boolean isAdmin() {
        return this.role.equals(Roles.ADMIN.name());
    }

    public boolean isPremium() {
        return this.role.equals(Roles.PREMIUM.name());
    }

    public void fromDTO(UpdateUserDTO dto) {
        this.name = dto.name();
        this.address = dto.address();
        this.phone = dto.phone();
    }

}
