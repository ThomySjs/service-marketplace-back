package com.servicemarketplace.api.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "services")
public class Service
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private User seller;
	@ManyToOne
	private Category category;
	@Column(nullable = false)
	private String title;
	private String description;
	@Column(name = "image_path")
	private String imagePath;
	@Column(nullable = false)
	@Builder.Default
	private Double price = 0.0;
	@Builder.Default
	private boolean deleted = false;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Builder.Default
	private ServiceStatus status = ServiceStatus.PENDING;
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate = LocalDateTime.now();
	@ManyToOne
	@JoinColumn(name = "service_reject_cause_id")
	private ServiceRejectCause serviceRejectCause;

	@PrePersist
	public void prePersist() {
		this.createdDate = LocalDateTime.now();
	}
}
