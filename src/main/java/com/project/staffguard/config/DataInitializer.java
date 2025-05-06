package com.project.staffguard.config;

import com.project.staffguard.model.Role;
import com.project.staffguard.model.User;
import com.project.staffguard.repository.RoleRepository;
import com.project.staffguard.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final RoleRepository roleRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    @PostConstruct
    public void init() {
        // Create roles
        Role adminRole = roleRepo.findByName("ROLE_ADMIN")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("ROLE_ADMIN");
                    return roleRepo.save(r);
                });

        Role hrRole = roleRepo.findByName("ROLE_HR_STAFF")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("ROLE_HR_STAFF");
                    return roleRepo.save(r);
                });

        Role empRole = roleRepo.findByName("ROLE_EMPLOYEE")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("ROLE_EMPLOYEE");
                    return roleRepo.save(r);
                });

        // Clear existing users
        userRepo.deleteAll();

        // Create admin
        saveUser("admin", "adminpass", Set.of(adminRole));

        // Create 5 normal users
        saveUser("alice", "password1", Set.of(empRole));
        saveUser("bob",   "password2", Set.of(empRole));
        saveUser("carol", "password3", Set.of(empRole));
        saveUser("dave",  "password4", Set.of(empRole));
        saveUser("eve",   "password5", Set.of(empRole));
    }

    private void saveUser(String username, String rawPassword, Set<Role> roles) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(rawPassword));
        user.setRoles(roles);
        userRepo.save(user);
    }
}
