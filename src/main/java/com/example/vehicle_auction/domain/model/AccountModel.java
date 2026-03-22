package com.example.vehicle_auction.domain.model;

import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class AccountModel {
    private UUID id;
    private String email;
    private String password;
    private boolean verified;
    private String verificationToken;
    private String resetPasswordToken;
    private LocalDateTime resetPasswordTokenExpiry;
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

    public AccountModel(UUID id,
                        String email,
                        String password,
                        boolean verified,
                        String verificationToken,
                        String resetPasswordToken,
                        LocalDateTime resetPasswordTokenExpiry,
                        int failedAttemptCount,
                        LocalDateTime lastLoginAt,
                        boolean system,
                        boolean active,
                        LocalDateTime createdAt,
                        LocalDateTime updatedAt,
                        String createdBy,
                        String updatedBy,
                        boolean deleted,
                        LocalDateTime deletedAt,
                        Set<RoleModel> roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.verified = verified;
        this.verificationToken = verificationToken;
        this.resetPasswordToken = resetPasswordToken;
        this.resetPasswordTokenExpiry = resetPasswordTokenExpiry;
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
