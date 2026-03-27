package com.example.vehicle_auction.domain.model;

import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.base.FullModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class AccountModel extends FullModel {
    private String email;
    private String password;
    private boolean verified;
    private String verificationToken;
    private LocalDateTime verificationTokenExpiry;
    private String resetPasswordToken;
    private LocalDateTime resetPasswordTokenExpiry;
    private int failedAttemptCount;
    private LocalDateTime lastLoginAt;
    private boolean system;
    private boolean active;
    private Set<RoleModel> roles;

    public AccountModel() {
        super();
        this.roles = new HashSet<>();
    }

    public AccountModel(UUID id,
                        LocalDateTime createdAt,
                        LocalDateTime updatedAt,
                        String createdBy,
                        String updatedBy,
                        boolean deleted,
                        LocalDateTime deletedAt,
                        String email,
                        String password,
                        boolean verified,
                        String verificationToken,
                        LocalDateTime verificationTokenExpiry,
                        String resetPasswordToken,
                        LocalDateTime resetPasswordTokenExpiry,
                        int failedAttemptCount,
                        LocalDateTime lastLoginAt,
                        boolean system,
                        boolean active,
                        Set<RoleModel> roles) {
        super(id, createdAt, updatedAt, createdBy, updatedBy, deleted, deletedAt);
        this.email = email;
        this.password = password;
        this.verified = verified;
        this.verificationToken = verificationToken;
        this.verificationTokenExpiry = verificationTokenExpiry;
        this.resetPasswordToken = resetPasswordToken;
        this.resetPasswordTokenExpiry = resetPasswordTokenExpiry;
        this.failedAttemptCount = failedAttemptCount;
        this.lastLoginAt = lastLoginAt;
        this.system = system;
        this.active = active;
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
