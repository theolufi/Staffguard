// JwtUtilsTest.java
package com.project.staffguard;

import com.project.staffguard.config.JwtUtils;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils = new JwtUtils();

    @BeforeEach
    void setup() throws Exception {
        // Set secret using reflection
        Field secretField = JwtUtils.class.getDeclaredField("jwtSecret");
        secretField.setAccessible(true);
        secretField.set(jwtUtils, "test-secret-1234567890-1234567890-123456");

        // Set expiration using reflection
        Field expField = JwtUtils.class.getDeclaredField("jwtExpirationMs");
        expField.setAccessible(true);
        expField.set(jwtUtils, 3600000L);

        jwtUtils.init();
    }

    @Test
    void generateToken_ValidAuth_ReturnsToken() {
        UserDetails principal = new User(
                "user",
                "doesn't matter",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        String token = jwtUtils.generateToken(auth);

        assertNotNull(token);
        assertTrue(token.length() > 50);
    }
}