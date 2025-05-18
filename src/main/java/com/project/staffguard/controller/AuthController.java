// src/main/java/com/project/staffguard/controller/AuthController.java
package com.project.staffguard.controller;

import com.project.staffguard.config.JwtUtils;
import com.project.staffguard.dto.UserDTO;
import com.project.staffguard.model.User;
import com.project.staffguard.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authManager,
                          UserService userService,
                          JwtUtils jwtUtils) {
        this.authManager = authManager;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDTO dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO dto) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );
            User user = (User) auth.getPrincipal();
            String jwt = jwtUtils.generateToken(auth);

            // return both token *and* user
            Map<String,Object> body = Map.of(
                    "token", jwt,
                    "user", Map.of(
                            "id",       user.getId(),
                            "username", user.getUsername(),
                            "roles",    user.getRoles()
                                    .stream()
                                    .map(r->r.getName())
                                    .collect(Collectors.toList())
                    )
            );

            return ResponseEntity.ok(body);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        // After filter, principal is your UserDetails (your User entity)
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof User)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(principal);
    }
}
