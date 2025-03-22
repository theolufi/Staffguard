package com.project.staffguard.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    // --- Override UserDetails methods ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convert roles to GrantedAuthority objects
        return roles.stream()
                .map(role -> (GrantedAuthority) role::getName)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Set to false for account expiration logic
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Set to false for account locking logic
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Set to false for credential expiration logic
    }

    @Override
    public boolean isEnabled() {
        return true; // Set to false to deactivate users
    }
}