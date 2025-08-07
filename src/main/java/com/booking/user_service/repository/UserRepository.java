package com.booking.user_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.booking.user_service.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>,JpaSpecificationExecutor<User> {
	 Optional<User> findByUsername(String username);
}
