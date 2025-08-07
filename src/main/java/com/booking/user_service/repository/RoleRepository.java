package com.booking.user_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.booking.user_service.entity.Role;

public interface RoleRepository  extends JpaRepository<Role, Long>,JpaSpecificationExecutor<Role>{
    Optional<Role> findByName(String name);
}
