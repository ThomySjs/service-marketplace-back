package com.servicemarketplace.api.domain.entities;

import java.time.LocalDateTime;

import com.servicemarketplace.api.dto.auth.RecoveryCodeDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "recovery_codes")
public class RecoveryCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int code;
    @Column(nullable = false, unique = true)
    private String email;
    private boolean deleted;
    @Column(name = "created_at", columnDefinition = "TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6)")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.code = (int) ((Math.random() * ((999999 - 100000) + 1)) + 100000);
        this.deleted = false;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.createdAt.plusMinutes(5));
    }

    public void delete() {
        this.deleted = true;
    }

    public boolean isValid(RecoveryCodeDTO userCode) {
        return this.code == userCode.code() && this.email.equals(userCode.email());
    }
}
