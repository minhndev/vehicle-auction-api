package com.example.vehicle_auction.domain.model;

import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AccountModel {
    private UUID id;
    private String email;
    private String password;
    private boolean verified;
    private int failedAttemptCount;
    private LocalDateTime lastLoginAt;
    private boolean system;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private boolean deleted;
    private LocalDateTime deletedAt;
    private Set<RoleModel> roles;

    public AccountModel() {
        this.roles = new HashSet<>();
    }

    public AccountModel(UUID id, String email, String password, boolean verified, int failedAttemptCount, LocalDateTime lastLoginAt, boolean system, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy, boolean deleted, LocalDateTime deletedAt, Set<RoleModel> roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.verified = verified;
        this.failedAttemptCount = failedAttemptCount;
        this.lastLoginAt = lastLoginAt;
        this.system = system;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.deleted = deleted;
        this.deletedAt = deletedAt;
        this.roles = roles != null ? roles : new HashSet<>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public int getFailedAttemptCount() {
        return failedAttemptCount;
    }

    public void setFailedAttemptCount(int failedAttemptCount) {
        this.failedAttemptCount = failedAttemptCount;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Set<RoleModel> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleModel> roles) {
        this.roles = roles;
    }

    public void recordFailedLogin() {
        this.failedAttemptCount++;
        if (this.failedAttemptCount >= 5)
            this.active = false;
    }

    public void recordSuccessfulLogin() {
        if (!this.active)
            throw new AppException(ErrorCode.UNAUTHORIZED);
        this.failedAttemptCount = 0;
        this.lastLoginAt = LocalDateTime.now();
    }
}
