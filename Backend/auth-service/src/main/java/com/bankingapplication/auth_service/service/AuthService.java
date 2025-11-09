package com.bankingapplication.auth_service.service;

import com.bankingapplication.auth_service.dto.AuthResponse;
import com.bankingapplication.auth_service.dto.LoginRequest;
import com.bankingapplication.auth_service.dto.RegisterRequest;
import com.bankingapplication.auth_service.entity.User;
import com.bankingapplication.auth_service.exception.AuthException;
import com.bankingapplication.auth_service.repository.UserRepository;
import com.bankingapplication.auth_service.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
                       UserDetailsServiceImpl userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    public AuthResponse register(RegisterRequest request) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AuthException("Username is already taken", HttpStatus.CONFLICT);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException("Email is already registered", HttpStatus.CONFLICT);
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRoles(new HashSet<>(Collections.singleton("USER")));

        userRepository.save(user);

        // Generate JWT tokens
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        // Create additional claims for JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("roles", user.getRoles());

        String accessToken = jwtTokenProvider.generateToken(claims, userDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

        AuthResponse response = new AuthResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setTokenType("Bearer");
        response.setExpiresIn(3600L); // 1 hour
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setSuccess(true);
        return response;
    }

    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsernameOrEmail());

        User user = userRepository.findByUsernameOrEmail(loginRequest.getUsernameOrEmail(),
                loginRequest.getUsernameOrEmail())
                .orElseThrow(() -> new AuthException("User not found", HttpStatus.NOT_FOUND));

        // Create additional claims for JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("roles", user.getRoles());

        String accessToken = jwtTokenProvider.generateToken(claims, userDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

        AuthResponse response = new AuthResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setTokenType("Bearer");
        response.setExpiresIn(3600L); // 1 hour
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setSuccess(true);
        return response;
    }
}
