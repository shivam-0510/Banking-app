package com.bankingapplication.auth_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankingapplication.auth_service.dto.UserCreationRequest;
import com.bankingapplication.auth_service.dto.UserPreferencesRequest;
import com.bankingapplication.auth_service.dto.UserResponse;
import com.bankingapplication.auth_service.dto.UserUpdateRequest;
import com.bankingapplication.auth_service.security.CurrentUser;
import com.bankingapplication.auth_service.service.UserService;

import jakarta.validation.Valid;
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreationRequest request) {
        return new ResponseEntity<>(userService.createUser(request), HttpStatus.CREATED);
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN') or #username == authentication.name")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserById(username));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable String username,
            @Valid @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(username, request));
    }

    @PutMapping("/{username}/preferences")
    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    public ResponseEntity<UserResponse> updateUserPreferences(
            @PathVariable String username,
            @Valid @RequestBody UserPreferencesRequest request) {
        return ResponseEntity.ok(userService.updateUserPreferences(username, request));
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{username}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateUser(@PathVariable String username) {
        userService.deactivateUser(username);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{username}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> activateUser(@PathVariable String username) {
        userService.activateUser(username);
        return ResponseEntity.noContent().build();
    }

    // Endpoint for getting current user's information
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@CurrentUser UserDetails userDetails) {
        return ResponseEntity.ok(userService.getUserById(userDetails.getUsername()));
    }
}
