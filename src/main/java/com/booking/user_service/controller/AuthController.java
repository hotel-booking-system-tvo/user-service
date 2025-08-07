package com.booking.user_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booking.user_service.constant.AuthConstant;
import com.booking.user_service.dto.LoginRequest;
import com.booking.user_service.entity.payload.JwtResponse;
import com.booking.user_service.security.jwt.AppUserDetail;
import com.booking.user_service.security.jwt.JwtTokenProvider;

@RestController
@RequestMapping(value = AuthConstant.AUTH_CONTEXT_PATH)
public class AuthController {
	 @Autowired
	    private AuthenticationManager authenticationManager;

	    @Autowired
	    private JwtTokenProvider tokenProvider;

	    @PostMapping("/login")
	    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
	    	 Authentication authentication = authenticationManager.authenticate(
	                 new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
	         );

	         AppUserDetail userDetails = (AppUserDetail) authentication.getPrincipal();
	         String accessToken = tokenProvider.generateAccessToken(userDetails);
	         String refreshToken = tokenProvider.generateRefreshToken(userDetails);

	         return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken));
	    }
}
