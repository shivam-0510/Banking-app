package com.bankingapplication.auth_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bankingapplication.auth_service.dto.UserCreationRequest;
import com.bankingapplication.auth_service.dto.UserPreferencesRequest;
import com.bankingapplication.auth_service.dto.UserPreferencesResponse;
import com.bankingapplication.auth_service.dto.UserResponse;
import com.bankingapplication.auth_service.dto.UserUpdateRequest;
import com.bankingapplication.auth_service.entity.User;
import com.bankingapplication.auth_service.entity.UserPreferences;
import com.bankingapplication.auth_service.exception.AuthServiceException;
import com.bankingapplication.auth_service.repository.UserPreferencesRepository;
import com.bankingapplication.auth_service.repository.UserRepository;
import com.bankingapplication.auth_service.util.UserIdGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserPreferencesRepository preferencesRepository;

    @Transactional
    public UserResponse createUser(UserCreationRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthServiceException("Email already registered", HttpStatus.CONFLICT);
        }

        // Create username from email (could be modified to use another strategy)
        String username = request.getEmail().split("@")[0];
        int counter = 1;
        while (userRepository.existsByUsername(username)) {
            username = request.getEmail().split("@")[0] + counter++;
        }

        // Create and save the user
        User user = User.builder()
                .username(username)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .dateOfBirth(request.getDateOfBirth())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .zipCode(request.getZipCode())
                .country(request.getCountry())
                .build();

        User savedUser = userRepository.save(user);

        // Create default preferences
        UserPreferences preferences = UserPreferences.builder()
                .user(savedUser)
                .languagePreference("en")
                .themePreference("light")
                .notificationEmail(true)
                .notificationSms(false)
                .notificationPush(false)
                .twoFactorAuth(false)
                .build();

        preferencesRepository.save(preferences);

        return mapToUserResponse(savedUser, preferences);
    }

    public UserResponse getUserById(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthServiceException("User not found", HttpStatus.NOT_FOUND));

        UserPreferences preferences = preferencesRepository.findByUser(user)
                .orElse(null);

        return mapToUserResponse(user, preferences);
    }

    /**
     * Get user by ID or create a minimal user record if not found. This is
     * useful for handling authenticated requests from users who exist in
     * auth-service but not yet in user-service.
     */
    public UserResponse getUserByIdOrCreate(String username) {
        // Try to find existing user
        User user = userRepository.findByUsername(username).orElse(null);

        // If user doesn't exist, try finding by email
        if (user == null) {
            user = userRepository.findByEmail(username).orElse(null);
        }

        // If still no user found, throw exception
        if (user == null) {
            throw new AuthServiceException("User not found", HttpStatus.NOT_FOUND);
        }

        // Return existing user with preferences
        UserPreferences preferences = preferencesRepository.findByUser(user)
                .orElse(null);

        return mapToUserResponse(user, preferences);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserPreferences prefs = preferencesRepository.findByUser(user)
                            .orElse(null);
                    return mapToUserResponse(user, prefs);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse updateUser(String username, UserUpdateRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthServiceException("User not found", HttpStatus.NOT_FOUND));

        // Update fields if they are not null
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }
        if (request.getCity() != null) {
            user.setCity(request.getCity());
        }
        if (request.getState() != null) {
            user.setState(request.getState());
        }
        if (request.getZipCode() != null) {
            user.setZipCode(request.getZipCode());
        }
        if (request.getCountry() != null) {
            user.setCountry(request.getCountry());
        }
        if (request.getProfilePicture() != null) {
            user.setProfilePicture(request.getProfilePicture());
        }

        userRepository.save(user);

        UserPreferences preferences = preferencesRepository.findByUser(user)
                .orElse(null);

        return mapToUserResponse(user, preferences);
    }

    @Transactional
    public UserResponse updateUserPreferences(String username, UserPreferencesRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthServiceException("User not found", HttpStatus.NOT_FOUND));

        UserPreferences preferences = preferencesRepository.findByUser(user)
                .orElseGet(() -> {
                    UserPreferences newPrefs = UserPreferences.builder()
                            .user(user)
                            .languagePreference("en")
                            .themePreference("light")
                            .notificationEmail(true)
                            .notificationSms(false)
                            .notificationPush(false)
                            .twoFactorAuth(false)
                            .build();
                    return preferencesRepository.save(newPrefs);
                });

        // Update preferences if not null
        if (request.getLanguagePreference() != null) {
            preferences.setLanguagePreference(request.getLanguagePreference());
        }
        if (request.getThemePreference() != null) {
            preferences.setThemePreference(request.getThemePreference());
        }
        if (request.getNotificationEmail() != null) {
            preferences.setNotificationEmail(request.getNotificationEmail());
        }
        if (request.getNotificationSms() != null) {
            preferences.setNotificationSms(request.getNotificationSms());
        }
        if (request.getNotificationPush() != null) {
            preferences.setNotificationPush(request.getNotificationPush());
        }
        if (request.getTwoFactorAuth() != null) {
            preferences.setTwoFactorAuth(request.getTwoFactorAuth());
        }

        preferencesRepository.save(preferences);

        return mapToUserResponse(user, preferences);
    }

    @Transactional
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthServiceException("User not found", HttpStatus.NOT_FOUND));

        // Delete preferences first (due to foreign key)
        preferencesRepository.findByUser(user).ifPresent(preferencesRepository::delete);

        // Delete user
        userRepository.delete(user);
    }

    @Transactional
    public void deactivateUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthServiceException("User not found", HttpStatus.NOT_FOUND));

        user.setEnabled(false);
        userRepository.save(user);
    }

    // Helper method to map User and UserPreferences to UserResponse
    private UserResponse mapToUserResponse(User user, UserPreferences preferences) {
        UserPreferencesResponse preferencesResponse = null;
        if (preferences != null) {
            preferencesResponse = UserPreferencesResponse.builder()
                    .languagePreference(preferences.getLanguagePreference())
                    .notificationEmail(preferences.isNotificationEmail())
                    .notificationSms(preferences.isNotificationSms())
                    .notificationPush(preferences.isNotificationPush())
                    .twoFactorAuth(preferences.isTwoFactorAuth())
                    .themePreference(preferences.getThemePreference())
                    .updatedAt(preferences.getUpdatedAt())
                    .build();
        }

        return UserResponse.builder()
                .userId(user.getUsername()) // Using username as userId for compatibility
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .dateOfBirth(user.getDateOfBirth())
                .address(user.getAddress())
                .city(user.getCity())
                .state(user.getState())
                .zipCode(user.getZipCode())
                .country(user.getCountry())
                .profilePicture(user.getProfilePicture())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .active(user.isEnabled())
                .preferences(preferencesResponse)
                .build();
    }
}
