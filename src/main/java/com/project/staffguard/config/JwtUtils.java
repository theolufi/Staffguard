// src/main/java/com/project/staffguard/config/JwtUtils.java
package com.project.staffguard.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms}")
    private long jwtExpirationMs;

    private Key key;

    @PostConstruct
    public void init() {
        // Build an HMAC-SHA key from your secret
        key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(Authentication auth) {
        // Cast to the interface, not Spring’s internal User
        UserDetails user = (UserDetails) auth.getPrincipal();

        var now = new Date();
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", user.getAuthorities()
                        .stream()
                        .map(a -> a.getAuthority())
                        .collect(Collectors.toList()))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
}
