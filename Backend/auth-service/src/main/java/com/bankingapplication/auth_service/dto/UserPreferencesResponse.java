package com.bankingapplication.auth_service.dto;

import java.time.LocalDateTime;

public class UserPreferencesResponse {

    private String languagePreference;
    private boolean notificationEmail;
    private boolean notificationSms;
    private boolean notificationPush;
    private boolean twoFactorAuth;
    private String themePreference;
    private LocalDateTime updatedAt;

    // Constructors
    public UserPreferencesResponse() {
    }

    public UserPreferencesResponse(String languagePreference, boolean notificationEmail, boolean notificationSms,
                                   boolean notificationPush, boolean twoFactorAuth, String themePreference,
                                   LocalDateTime updatedAt) {
        this.languagePreference = languagePreference;
        this.notificationEmail = notificationEmail;
        this.notificationSms = notificationSms;
        this.notificationPush = notificationPush;
        this.twoFactorAuth = twoFactorAuth;
        this.themePreference = themePreference;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public String getLanguagePreference() {
        return languagePreference;
    }

    public void setLanguagePreference(String languagePreference) {
        this.languagePreference = languagePreference;
    }

    public boolean isNotificationEmail() {
        return notificationEmail;
    }

    public void setNotificationEmail(boolean notificationEmail) {
        this.notificationEmail = notificationEmail;
    }

    public boolean isNotificationSms() {
        return notificationSms;
    }

    public void setNotificationSms(boolean notificationSms) {
        this.notificationSms = notificationSms;
    }

    public boolean isNotificationPush() {
        return notificationPush;
    }

    public void setNotificationPush(boolean notificationPush) {
        this.notificationPush = notificationPush;
    }

    public boolean isTwoFactorAuth() {
        return twoFactorAuth;
    }

    public void setTwoFactorAuth(boolean twoFactorAuth) {
        this.twoFactorAuth = twoFactorAuth;
    }

    public String getThemePreference() {
        return themePreference;
    }

    public void setThemePreference(String themePreference) {
        this.themePreference = themePreference;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
