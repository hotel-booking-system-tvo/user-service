package com.booking.user_service.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.booking.user_service.dto.UserDto;
import com.booking.user_service.entity.Role;
import com.booking.user_service.entity.User;
import com.booking.user_service.repository.RoleRepository;
import com.booking.user_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public void register(UserDto userDto) {
		 if (userRepository.findByUsername(userDto.getUsername()).isPresent())
	            throw new RuntimeException("User already exists");

	        User user = new User();
	        user.setUsername(userDto.getUsername());
	        user.setEmail(userDto.getEmail());
	        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

	        Set<Role> roles = userDto.getRoles().stream()
	            .map(name -> roleRepository.findByName(name)
	                .orElseGet(() -> roleRepository.save(new Role(null, name))))
	            .collect(Collectors.toSet());

	        user.setRoles(roles);
	        userRepository.save(user);
		
	}

	@Override
	public UserDto getByUsername(String username) {
		  User user = userRepository.findByUsername(username)
		            .orElseThrow(() -> new RuntimeException("Not found"));

		        UserDto dto = new UserDto();
		        dto.setUsername(user.getUsername());
		        dto.setEmail(user.getEmail());
		        dto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
		        return dto;
	}

}
