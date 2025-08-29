package com.servicemarketplace.api.config;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.servicemarketplace.api.domain.entities.User;
import com.servicemarketplace.api.domain.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig
{
	private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
	private final AuthTokenFilter jwtAuthFilter;
	private final AuthenticationProvider authenticationProvider;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		http
				.csrf(AbstractHttpConfigurer::disable)
				.cors(Customizer.withDefaults())
				.authorizeHttpRequests(req ->
						req.requestMatchers("/auth/**",
										"/swagger-ui/**",
										"/v3/api-docs/**",
										"/swagger-ui.html",
										"/swagger-resources/**",
										"/webjars/**").permitAll()
								.anyRequest().authenticated()
						)
				.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.logout(logout -> logout.logoutUrl("/auth/logout").addLogoutHandler(((request, response, authentication) -> {
					logout(jwtUtils.parseJwt(request));
				})).logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext())))
		;

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of("http://localhost:5173"));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(List.of("*"));
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	private void logout(String token)
	{
		if (token == null) {
			throw new IllegalArgumentException("Invalid token");
		}

		final String username = jwtUtils.getUserFromToken(token);
        final Optional<User> user = userRepository.findByName(username);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Invalid user");
        }

        final User foundUser = user.get();
        final String revokedToken = jwtUtils.generateToken(foundUser.getName(), TokenTypes.REVOKED);
        foundUser.setToken(revokedToken);
		userRepository.save(foundUser);
		log.info("Logout exitoso, token: {}", revokedToken);
	}
}