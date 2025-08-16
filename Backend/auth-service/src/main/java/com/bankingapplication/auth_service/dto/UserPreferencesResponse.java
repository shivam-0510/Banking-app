package com.bankingapplication.auth_service.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPreferencesResponse {

    private String languagePreference;
    private boolean notificationEmail;
    private boolean notificationSms;
    private boolean notificationPush;
    private boolean twoFactorAuth;
    private String themePreference;
    private LocalDateTime updatedAt;
}
