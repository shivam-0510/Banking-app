package com.bankingapplication.auth_service.service;

import com.bankingapplication.auth_service.entity.User;
import com.bankingapplication.auth_service.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        // Given
        String username = "testuser";
        User user = new User();
        user.setId(1L);
        user.setUsername(username);
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setEnabled(true);
        user.setRoles(new HashSet<>());

        when(userRepository.findByUsernameOrEmail(username, username))
                .thenReturn(Optional.of(user));

        // When
        UserDetails result = userDetailsService.loadUserByUsername(username);

        // Then
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(userRepository).findByUsernameOrEmail(username, username);
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserNotFound() {
        // Given
        String username = "nonexistent";
        when(userRepository.findByUsernameOrEmail(username, username))
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(username));
        verify(userRepository).findByUsernameOrEmail(username, username);
    }
}
