package com.example.springboot_security_jwt.controller;

import com.example.springboot_security_jwt.jwt.JwtTokenProvider;
import com.example.springboot_security_jwt.model.entity.ERole;
import com.example.springboot_security_jwt.model.entity.Roles;
import com.example.springboot_security_jwt.model.entity.Users;
import com.example.springboot_security_jwt.model.service.RoleService;
import com.example.springboot_security_jwt.model.service.UserService;
import com.example.springboot_security_jwt.payload.request.LoginRequest;
import com.example.springboot_security_jwt.payload.request.SignupRequest;
import com.example.springboot_security_jwt.payload.response.JwtResponse;
import com.example.springboot_security_jwt.payload.response.MessageResponse;
import com.example.springboot_security_jwt.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/auth")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder encoder;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        if (userService.existsByUserName(signupRequest.getUserName())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already"));
        }
        if (userService.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already"));
        }
        Users users = new Users();
        users.setUserName(signupRequest.getUserName());
        users.setPassword(encoder.encode((signupRequest.getPassword())));
        users.setEmail(signupRequest.getEmail());
        users.setPhone(signupRequest.getPhone());
        users.setUserStatus(true);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date now = new Date();
        String dateNow = sdf.format(now);
        try {
            users.setCreated( sdf.parse(dateNow));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Set<String> strRoles = signupRequest.getListRole();
        Set<Roles> listRoles = new HashSet<>();
        if (strRoles == null) {
            // User quyền mặc định
            Roles userRole = roleService.findByRoleName(ERole.ROLE_USER).orElseThrow(
                    () -> new RuntimeException("Error: Role is not found."));
            listRoles.add(userRole);

        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Roles adminRole = roleService.findByRoleName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        listRoles.add(adminRole);
                    case "moderator":
                        Roles modRole = roleService.findByRoleName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        listRoles.add(modRole);
                    case "user":
                        Roles userRole = roleService.findByRoleName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        listRoles.add(userRole);
                }
            });
        }
        users.setListRoles(listRoles);
        userService.saveOrUpdate(users);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String jwt = tokenProvider.generateToken(customUserDetails);
        List<String> listRoles = customUserDetails.getAuthorities().stream()
                .map(item -> item.getAuthority()).collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwt, customUserDetails.getUserId(),
                customUserDetails.getUsername(), customUserDetails.getEmail(), customUserDetails.getPhone(), listRoles));
    }
}
