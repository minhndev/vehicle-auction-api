package com.example.vehicle_auction.infrastructure.persistence.repository;

import com.example.vehicle_auction.domain.model.UserModel;
import com.example.vehicle_auction.domain.repository.UserRepository;
import com.example.vehicle_auction.infrastructure.persistence.entity.Account;
import com.example.vehicle_auction.infrastructure.persistence.entity.Role;
import com.example.vehicle_auction.infrastructure.persistence.mapper.AccountEntityMapper;
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
    private final AccountEntityMapper accountEntityMapper;

    @Override
    public UserModel save(UserModel userModel) {
        User userEntity;

        if (userModel.getId() != null && jpaUserRepository.existsById(userModel.getId())) {
            userEntity = jpaUserRepository.findById(userModel.getId()).get();
            userEntityMapper.updateEntityFromModel(userModel, userEntity);
        } else {
            userEntity = userEntityMapper.toEntity(userModel);
        }

        if (userModel.getAccount() != null) {
            Account accountEntity;

            if (userModel.getAccount().getId() != null && jpaAccountRepository.existsById(userModel.getAccount().getId())) {
                accountEntity = jpaAccountRepository.findById(userModel.getAccount().getId()).get();
                accountEntityMapper.updateEntityFromModel(userModel.getAccount(), accountEntity);
            } else {
                accountEntity = accountEntityMapper.toEntity(userModel.getAccount());
            }

            if (userModel.getAccount().getRoles() != null) {
                Set<Role> managedRoles = userModel.getAccount().getRoles().stream()
                        .map(roleModel -> jpaRoleRepository.getReferenceById(roleModel.getId()))
                        .collect(Collectors.toSet());
                accountEntity.setRoles(managedRoles);
            }

            accountEntity = jpaAccountRepository.save(accountEntity);
            userEntity.setAccount(accountEntity);
        }

        User savedEntity = jpaUserRepository.save(userEntity);
        return userEntityMapper.toDomain(savedEntity);
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
    public Optional<UserModel> findByIdAndDeletedFalse(UUID userId) {
        return jpaUserRepository.findByIdAndDeletedFalse(userId)
                .map(userEntityMapper::toDomain);
    }

    @Override
    public Optional<UserModel> findByIdAndDeletedTrue(UUID userId) {
        return jpaUserRepository.findByIdAndDeletedTrue(userId)
                .map(userEntityMapper::toDomain);
    }

    @Override
    public Page<UserModel> findAll(Pageable pageable, String keyword, Boolean active, Boolean verified, Boolean deleted) {
        return jpaUserRepository.searchUsers(pageable, keyword, active, verified, deleted)
                .map(userEntityMapper::toDomain);
    }
}
