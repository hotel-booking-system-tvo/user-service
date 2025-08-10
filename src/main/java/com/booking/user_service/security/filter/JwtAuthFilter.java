package com.booking.user_service.security.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.booking.user_service.security.jwt.JwtTokenProvider;
import com.booking.user_service.security.service.AppUserDetailService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtUtils;
	private final AppUserDetailService appUserDetailService;
	private static final List<String> PUBLIC_URLS = List.of(
		    "/auth/**",
		    "/user/register",
		    "/swagger-ui.html",
		    "/swagger-ui/**",
		    "/v3/api-docs/**"
		);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		String path = request.getServletPath();
	    if (PUBLIC_URLS.stream().anyMatch(path::startsWith)) {
	        filterChain.doFilter(request, response);
	        return;
	    }

		if (header != null && header.startsWith("Bearer ")) {
			String token = header.substring(7); // Bỏ "Bearer "

			if (jwtUtils.validateToken(token)) {
				String userId = jwtUtils.getUserNameFromToken(token);

				UserDetails userDetails = appUserDetailService.loadUserByUsername(userId);

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		filterChain.doFilter(request, response);
	}

}
