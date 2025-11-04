package com.servicemarketplace.api.domain.entities;

public enum ServiceStatus
{
	PENDING("Pendiente"),
	APPROVED("Aprobado"),
	REJECTED("Rechazado");

	private final String translation;

	ServiceStatus(String translation) {
		this.translation = translation;
	}

	public String getTranslation() {
		return translation;
	}
}
