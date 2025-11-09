package com.bankingapplication.auth_service.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
@Entity
@Table(name = "user_preferences")
public class UserPreferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "language_preference")
    private String languagePreference;

    @Column(name = "notification_email")
    private boolean notificationEmail;

    @Column(name = "notification_sms")
    private boolean notificationSms;

    @Column(name = "notification_push")
    private boolean notificationPush;

    @Column(name = "two_factor_auth")
    private boolean twoFactorAuth;

    @Column(name = "theme_preference")
    private String themePreference;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        // Set defaults
        if (this.languagePreference == null) {
            this.languagePreference = "en";
        }
        if (this.themePreference == null) {
            this.themePreference = "light";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Constructors
    public UserPreferences() {
    }

    public UserPreferences(Long id, User user, String languagePreference, boolean notificationEmail,
                          boolean notificationSms, boolean notificationPush, boolean twoFactorAuth,
                          String themePreference, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.languagePreference = languagePreference;
        this.notificationEmail = notificationEmail;
        this.notificationSms = notificationSms;
        this.notificationPush = notificationPush;
        this.twoFactorAuth = twoFactorAuth;
        this.themePreference = themePreference;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
