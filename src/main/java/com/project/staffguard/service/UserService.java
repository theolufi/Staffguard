package com.project.staffguard.service;

import com.project.staffguard.dto.UserDTO;
import com.project.staffguard.model.Role;
import com.project.staffguard.model.User;
import com.project.staffguard.repository.RoleRepository;
import com.project.staffguard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;   // <— correct import

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Transactional
    public User createUser(UserDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username already taken");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());

        Set<Role> roles = dto.getRoles().stream()
                .map(roleName ->
                        roleRepository.findByName(roleName)
                                .orElseThrow(() -> new RuntimeException("Role not found: ROLE_" + roleName))
                )
                .collect(Collectors.toSet());
        user.setRoles(roles);

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public User updateUser(Long id, UserDTO dto) {
        User existing = getUserById(id);

        if (dto.getUsername() != null && !dto.getUsername().equals(existing.getUsername())) {
            if (userRepository.existsByUsername(dto.getUsername())) {
                throw new RuntimeException("Username already taken");
            }
            existing.setUsername(dto.getUsername());
        }

        if (dto.getPassword() != null) {
            existing.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getEmail() != null) {
            existing.setEmail(dto.getEmail());
        }

        if (dto.getRoles() != null) {
            Set<Role> roles = dto.getRoles().stream()
                    .map(roleName ->
                            roleRepository.findByName(roleName)
                                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName))
                    )
                    .collect(Collectors.toSet());
            existing.setRoles(roles);
        }

        return userRepository.save(existing);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }
}
