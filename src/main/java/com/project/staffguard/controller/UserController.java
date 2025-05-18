package com.project.staffguard.controller;

import com.project.staffguard.dto.UserDTO;
import com.project.staffguard.model.User;
import com.project.staffguard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Create
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> createUser(@RequestBody UserDTO dto) {
        User created = userService.createUser(dto);
        return ResponseEntity.ok(created);
    }

    // Read all
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Read one
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or principal.id == #id")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // Update
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or principal.id == #id")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody UserDTO dto
    ) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    // Delete
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
