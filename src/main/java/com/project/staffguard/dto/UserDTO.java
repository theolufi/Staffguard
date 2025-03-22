package com.project.staffguard.dto;

import lombok.Data;
import java.util.Set;

@Data
public class UserDTO {
    private String username;
    private String password;
    private Set<String> roles; // e.g., ["HR_STAFF", "ADMIN"]
}