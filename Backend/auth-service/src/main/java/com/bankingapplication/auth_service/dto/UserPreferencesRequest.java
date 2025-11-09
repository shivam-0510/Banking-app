package com.bankingapplication.auth_service.dto;

public class UserPreferencesRequest {

    private String languagePreference;
    private Boolean notificationEmail;
    private Boolean notificationSms;
    private Boolean notificationPush;
    private Boolean twoFactorAuth;
    private String themePreference;

    // Constructors
    public UserPreferencesRequest() {
    }

    public UserPreferencesRequest(String languagePreference, Boolean notificationEmail, Boolean notificationSms,
                                  Boolean notificationPush, Boolean twoFactorAuth, String themePreference) {
        this.languagePreference = languagePreference;
        this.notificationEmail = notificationEmail;
        this.notificationSms = notificationSms;
        this.notificationPush = notificationPush;
        this.twoFactorAuth = twoFactorAuth;
        this.themePreference = themePreference;
    }

    // Getters and Setters
    public String getLanguagePreference() {
        return languagePreference;
    }

    public void setLanguagePreference(String languagePreference) {
        this.languagePreference = languagePreference;
    }

    public Boolean getNotificationEmail() {
        return notificationEmail;
    }

    public void setNotificationEmail(Boolean notificationEmail) {
        this.notificationEmail = notificationEmail;
    }

    public Boolean getNotificationSms() {
        return notificationSms;
    }

    public void setNotificationSms(Boolean notificationSms) {
        this.notificationSms = notificationSms;
    }

    public Boolean getNotificationPush() {
        return notificationPush;
    }

    public void setNotificationPush(Boolean notificationPush) {
        this.notificationPush = notificationPush;
    }

    public Boolean getTwoFactorAuth() {
        return twoFactorAuth;
    }

    public void setTwoFactorAuth(Boolean twoFactorAuth) {
        this.twoFactorAuth = twoFactorAuth;
    }

    public String getThemePreference() {
        return themePreference;
    }

    public void setThemePreference(String themePreference) {
        this.themePreference = themePreference;
    }
}
