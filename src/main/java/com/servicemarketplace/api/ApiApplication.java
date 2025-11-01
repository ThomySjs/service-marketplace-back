package com.servicemarketplace.api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import com.mercadopago.MercadoPagoConfig;
import com.servicemarketplace.api.config.CustomConfig.MpConfig;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
@EnableAsync
public class ApiApplication implements CommandLineRunner {

	private final MpConfig mpConfig;

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		MercadoPagoConfig.setAccessToken(mpConfig.getAccessToken());
	}

}
