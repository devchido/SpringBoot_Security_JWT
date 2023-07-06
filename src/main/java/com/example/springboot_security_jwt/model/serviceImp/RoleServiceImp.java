package com.example.springboot_security_jwt.model.serviceImp;

import com.example.springboot_security_jwt.model.entity.ERole;
import com.example.springboot_security_jwt.model.entity.Roles;
import com.example.springboot_security_jwt.model.repository.RoleRepository;
import com.example.springboot_security_jwt.model.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImp implements RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public Optional<Roles> findByRoleName(ERole roleName) {
        return roleRepository.findByRoleName(roleName);
    }
}
