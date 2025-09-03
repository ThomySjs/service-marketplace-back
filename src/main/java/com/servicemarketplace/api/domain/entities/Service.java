package com.servicemarketplace.api.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "services")
public class Service {
    @Id
    private Long id;
    @ManyToOne
    private User seller;
    @ManyToOne
    private Category category;
    @Column(nullable = false)
    private String title;
    private String description;
    @Column(nullable = false)
    @Builder.Default
    private Double price = 0.0;
    @Builder.Default
    private boolean deleted = false;
}
