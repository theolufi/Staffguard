package com.project.staffguard.controller;

import com.project.staffguard.dto.UserDTO;
import com.project.staffguard.model.User;
import com.project.staffguard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDTO dto) {
        return ResponseEntity.ok(userService.registerUser(dto));
    }
}