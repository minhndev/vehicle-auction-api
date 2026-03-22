package com.example.vehicle_auction.infrastructure.persistence.repository;

import com.example.vehicle_auction.domain.model.AccountModel;
import com.example.vehicle_auction.domain.model.UserModel;
import com.example.vehicle_auction.domain.repository.UserRepository;
import com.example.vehicle_auction.infrastructure.persistence.entity.Account;
import com.example.vehicle_auction.infrastructure.persistence.entity.Role;
import com.example.vehicle_auction.infrastructure.persistence.mapper.UserEntityMapper;
import com.example.vehicle_auction.infrastructure.persistence.entity.User;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaAccountRepository;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaRoleRepository;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository jpaUserRepository;
    private final JpaRoleRepository jpaRoleRepository;
    private final JpaAccountRepository jpaAccountRepository;
    private final UserEntityMapper userEntityMapper;

    @Override
    public UserModel save(UserModel userModel) {
        User entity = resolveExistingUser(userModel)
                .orElseGet(() -> userEntityMapper.toEntity(userModel));

        mergeUserFields(entity, userModel);
        syncAccountRoles(entity, userModel);

        User savedEntity = jpaUserRepository.save(entity);
        return userEntityMapper.toDomain(savedEntity);
    }

    private Optional<User> resolveExistingUser(UserModel userModel) {
        if (userModel.getId() != null) {
            Optional<User> existingByUserId = jpaUserRepository.findById(userModel.getId());
            if (existingByUserId.isPresent()) {
                return existingByUserId;
            }
        }

        if (userModel.getAccount() != null && userModel.getAccount().getId() != null) {
            return jpaUserRepository.findByAccountId(userModel.getAccount().getId());
        }

        return Optional.empty();
    }

    private void mergeUserFields(User entity, UserModel userModel) {
        entity.setFirstName(userModel.getFirstName());
        entity.setLastName(userModel.getLastName());
        entity.setIdentityNumber(userModel.getIdentityNumber());
        entity.setBirthdate(userModel.getBirthdate());
        entity.setGender(userModel.getGender());
        entity.setPhoneNumber(userModel.getPhoneNumber());
        entity.setAddress(userModel.getAddress());
        entity.setAvatarURL(userModel.getAvatarURL());

        if (userModel.getAccount() == null) {
            return;
        }

        if (entity.getAccount() == null) {
            UUID accountId = userModel.getAccount().getId();
            if (accountId != null) {
                entity.setAccount(jpaAccountRepository.getReferenceById(accountId));
            } else {
                entity.setAccount(userEntityMapper.toEntity(userModel).getAccount());
            }
            return;
        }

        mergeAccountFields(entity.getAccount(), userModel.getAccount());
    }

    private void mergeAccountFields(Account entityAccount, AccountModel accountModel) {
        entityAccount.setEmail(accountModel.getEmail());
        entityAccount.setPassword(accountModel.getPassword());
        entityAccount.setVerified(accountModel.isVerified());
        entityAccount.setVerificationToken(accountModel.getVerificationToken());
        entityAccount.setResetPasswordToken(accountModel.getResetPasswordToken());
        entityAccount.setResetPasswordTokenExpiry(accountModel.getResetPasswordTokenExpiry());
        entityAccount.setFailedAttemptCount(accountModel.getFailedAttemptCount());
        entityAccount.setLastLoginAt(accountModel.getLastLoginAt());
        entityAccount.setSystem(accountModel.isSystem());
        entityAccount.setActive(accountModel.isActive());
    }

    private void syncAccountRoles(User entity, UserModel userModel) {
        if (entity.getAccount() == null || userModel.getAccount() == null || userModel.getAccount().getRoles() == null) {
            return;
        }

        Set<Role> managedRoles = userModel.getAccount().getRoles().stream()
                .map(roleModel -> jpaRoleRepository.getReferenceById(roleModel.getId()))
                .collect(Collectors.toSet());

        entity.getAccount().setRoles(managedRoles);
    }

    @Override
    public Optional<UserModel> findByAccountId(UUID accountId) {
        return jpaUserRepository.findByAccountId(accountId)
                .map(userEntityMapper::toDomain);
    }

    @Override
    public Optional<UserModel> findById(UUID userId) {
        return jpaUserRepository.findDetailById(userId)
                .map(userEntityMapper::toDomain);
    }

    @Override
    public Page<UserModel> findAll(Pageable pageable, String keyword, Boolean active, Boolean verified, Boolean deleted) {
        return jpaUserRepository.searchUsers(pageable, keyword, active, verified, deleted)
                .map(userEntityMapper::toDomain);
    }
}
