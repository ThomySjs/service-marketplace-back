package com.servicemarketplace.api.domain.entities;

import com.servicemarketplace.api.dto.membership.MembershipDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "memberships")
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "El nombre no puede ser nulo.")
    @NotBlank(message = "El nombre no puede estar vacio.")
    private String name;
    @Builder.Default
    @NotNull(message = "El precio no puede ser nulo.")
    private Double price = 0.0;
    private String description;

    public void fromDto(MembershipDTO dto) {
        this.name = dto.name();
        this.price = dto.price();
        this.description = dto.description();
    }
}
