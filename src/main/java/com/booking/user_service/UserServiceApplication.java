package com.booking.user_service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.booking.user_service.entity.Role;
import com.booking.user_service.repository.RoleRepository;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}
	 @Bean
	    CommandLineRunner initRoles(RoleRepository roleRepository) {
	        return args -> {
	            if (roleRepository.findByName("ROLE_USER").isEmpty()) {
	                roleRepository.save(new Role("ROLE_USER"));
	            }
	            if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
	                roleRepository.save(new Role("ROLE_ADMIN"));
	            }
	        };
	    }

}
