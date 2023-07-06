package com.example.springboot_security_jwt.model.repository;

import com.example.springboot_security_jwt.model.entity.ERole;
import com.example.springboot_security_jwt.model.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Integer> {
    Optional<Roles> findByRoleName(ERole roleName);

}
