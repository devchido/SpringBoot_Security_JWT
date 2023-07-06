package com.example.springboot_security_jwt.model.service;

import com.example.springboot_security_jwt.model.entity.ERole;
import com.example.springboot_security_jwt.model.entity.Roles;

import java.util.Optional;

public interface RoleService {
    Optional<Roles> findByRoleName(ERole roleName);
}
