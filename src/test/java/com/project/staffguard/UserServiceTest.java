package com.project.staffguard;

import com.project.staffguard.dto.UserDTO;
import com.project.staffguard.model.Role;
import com.project.staffguard.model.User;
import com.project.staffguard.repository.RoleRepository;
import com.project.staffguard.repository.UserRepository;
import com.project.staffguard.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks private UserService userService;
    // Updated UserDTO instantiation using setters
    UserDTO dto = new UserDTO();

    @Test
    void createUser_Success() {
        // given
        UserDTO dto = new UserDTO();
        dto.setUsername("user");
        dto.setPassword("pass");
        dto.setEmail("email");
        dto.setRoles(Set.of("ADMIN"));

        when(userRepository.existsByUsername("user")).thenReturn(false);
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(new Role()));
        when(passwordEncoder.encode("pass")).thenReturn("encodedPass");
        // <<< STUB THE SAVE() CALL TO RETURN A NON-NULL USER >>>
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        User result = userService.createUser(dto);

        // then
        assertNotNull(result);
        verify(userRepository).save(any(User.class));
    }


    @Test
    void createUser_UsernameExists_ThrowsException() {
        UserDTO dto = new UserDTO();
        dto.setUsername("user");
        dto.setPassword("pass");
        dto.setEmail("email");
        dto.setRoles(Set.of("ADMIN"));
        when(userRepository.existsByUsername("user")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.createUser(dto));
    }

    @Test
    void getUserById_NotFound_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.getUserById(1L));
    }
}