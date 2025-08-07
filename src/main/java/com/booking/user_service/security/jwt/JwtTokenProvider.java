package com.booking.user_service.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Slf4j
@Component
public class JwtTokenProvider {
	@Value("${apps.security.secret}")
    private String jwtSecret;

    @Value("${apps.security.token-prefix}")
    private String tokenPrefix;

    @Value("${apps.security.jwtExpirationMs}")
    private long jwtExpirationMs;

    @Value("${apps.security.refreshJwtExpirationMs}")
    private long refreshJwtExpirationMs;

    public String generateAccessToken(AppUserDetail userPrincipal) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());

        return JWT.create()
        		// co the getId hoac getUserName hoac email
                .withSubject(userPrincipal.getUsername().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .withClaim("roles", getRoles(userPrincipal))
                .sign(algorithm);
    }

    public String generateRefreshToken(AppUserDetail userPrincipal) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());

        return JWT.create()
                .withSubject(userPrincipal.getUsername().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshJwtExpirationMs))
                .withClaim("roles", getRoles(userPrincipal))
                .sign(algorithm);
    }

    public boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
            JWT.require(algorithm).build().verify(token);
            return true;
        } catch (Exception e) {
            log.error("JWT validation failed: {}", e.getMessage());
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getExpiresAt().before(new Date());
        } catch (Exception e) {
            log.error("Error checking token expiration: {}", e.getMessage());
            return true;
        }
    }

    public String getUserNameFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getSubject(); // usually ID or username
        } catch (Exception e) {
            log.error("Failed to decode token subject: {}", e.getMessage());
            return null;
        }
    }

    public List<String> getRolesFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("roles").asList(String.class);
        } catch (Exception e) {
            log.error("Failed to extract roles: {}", e.getMessage());
            return null;
        }
    }

    private List<String> getRoles(AppUserDetail userPrincipal) {
        return userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}
