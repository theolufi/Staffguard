// src/main/java/com/project/staffguard/config/JwtAuthenticationFilter.java
package com.project.staffguard.config;

import com.project.staffguard.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserService userService;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserService userService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
            throws ServletException, IOException
    {
        String header = req.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Jws<Claims> j = jwtUtils.parseToken(token);
                Claims claims = j.getBody();
                String username = claims.getSubject();

                // Load *your* User entity, which implements UserDetails
                UserDetails userDetails = userService.loadUserByUsername(username);

                // Rebuild authorities from the token (or from userDetails.getAuthorities())
                @SuppressWarnings("unchecked")
                List<String> roles = (List<String>) claims.get("roles");
                var auth = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        roles.stream()
                                .map(r -> new org.springframework.security.core.authority.SimpleGrantedAuthority(r))
                                .collect(Collectors.toList())
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (JwtException ex) {
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(req, res);
    }
}
