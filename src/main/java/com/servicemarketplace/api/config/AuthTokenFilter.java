package com.servicemarketplace.api.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter
{

	private final JwtUtils jwtUtils;
	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException
	{
		try
		{
			if (request.getServletPath().contains("/auth"))
			{
				filterChain.doFilter(request, response);
				return;
			}

			String jwt = jwtUtils.parseJwt(request);

			if (jwt != null && jwtUtils.validateToken(jwt) && jwtUtils.validateSession(jwt))
			{
				String username = jwtUtils.getUserFromToken(jwt);

				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null)
				{
					UserDetails userDetails = userDetailsService.loadUserByUsername(username);
					final var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
			filterChain.doFilter(request, response);
		}
		catch (UsernameNotFoundException e)
		{
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario no encontrado");
		}
	}
}
