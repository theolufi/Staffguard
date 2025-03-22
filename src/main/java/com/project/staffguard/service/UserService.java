package com.project.staffguard.service;

import com.project.staffguard.dto.UserDTO;
import com.project.staffguard.model.Role;
import com.project.staffguard.model.User;
import com.project.staffguard.repository.RoleRepository;
import com.project.staffguard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(UserDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        Set<Role> roles = dto.getRoles().stream()
                .map(roleName ->
                        roleRepository.findByName("ROLE_" + roleName)
                                .orElseThrow(() -> new RuntimeException("Role not found: ROLE_" + roleName))
                )
                .collect(Collectors.toSet());

        user.setRoles(roles);
        return userRepository.save(user);
    }
}