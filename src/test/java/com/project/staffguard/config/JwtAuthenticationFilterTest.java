package com.project.staffguard.config;

import com.project.staffguard.model.Role;
import com.project.staffguard.model.User;
import com.project.staffguard.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock HttpServletRequest request;
    @Mock HttpServletResponse response;
    @Mock JwtUtils jwtUtils;
    @Mock UserService userService;
    @Mock Jws<Claims> jws;
    @Mock Claims claims;

    private MockFilterChain filterChain;
    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setup() {
        filter = new JwtAuthenticationFilter(jwtUtils, userService);
        filterChain = new MockFilterChain();
        SecurityContextHolder.clearContext();
    }

    @Test
    void validToken_SetsAuthentication() throws Exception {
        // Arrange
        Mockito.when(request.getHeader("Authorization"))
                .thenReturn("Bearer valid.token");
        Mockito.when(jwtUtils.parseToken("valid.token"))
                .thenReturn(jws);
        Mockito.when(jws.getBody()).thenReturn(claims);
        Mockito.when(claims.getSubject()).thenReturn("testuser");

        // Create a real user with roles set
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        Role role = new Role();
        role.setName("ROLE_USER");
        user.setRoles(Set.of(role));

        // Return the fully constructed user
        Mockito.when(userService.loadUserByUsername("testuser"))
                .thenReturn(user);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNotNull(SecurityContextHolder.getContext().getAuthentication(),
                "Authentication should be set in SecurityContext");
        assertEquals("testuser",
                SecurityContextHolder.getContext().getAuthentication().getName());
    }

}
