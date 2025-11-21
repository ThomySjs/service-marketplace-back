package com.servicemarketplace.api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mercadopago.MercadoPagoConfig;
import com.servicemarketplace.api.config.Roles;
import com.servicemarketplace.api.config.CustomConfig.DefaultUserConfig;
import com.servicemarketplace.api.config.CustomConfig.ImageConfig;
import com.servicemarketplace.api.config.CustomConfig.MpConfig;
import com.servicemarketplace.api.domain.entities.User;
import com.servicemarketplace.api.domain.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
@EnableAsync
@EnableScheduling
public class ApiApplication implements CommandLineRunner {

	private final MpConfig mpConfig;
	private final UserRepository userRepository;
	private final DefaultUserConfig defaultUserConfig;
	private final PasswordEncoder passwordEncoder;
	private final ImageConfig imageConfig;

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		MercadoPagoConfig.setAccessToken(mpConfig.getAccessToken());
		createDefaultAdmin();
	}

	private void createDefaultAdmin() {
		if (userRepository.findByEmail(defaultUserConfig.getEmail()).get().equals(null)) {
			System.out.println("Creando usuario admin");
			System.out.println(defaultUserConfig.getPassword());
			User admin = User.builder()
				.name(defaultUserConfig.getName())
				.password(passwordEncoder.encode(defaultUserConfig.getPassword()))
				.email(defaultUserConfig.getEmail())
				.role(Roles.ADMIN.name())
				.verified(true)
				.imagePath(imageConfig.getDefaultImage())
				.address(null)
				.phone(null)
				.zoneId(null)
				.build();

			userRepository.save(admin);
		}
	}
}
