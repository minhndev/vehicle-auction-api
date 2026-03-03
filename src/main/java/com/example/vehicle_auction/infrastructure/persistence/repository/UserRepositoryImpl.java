package com.example.vehicle_auction.infrastructure.persistence.repository;

import com.example.vehicle_auction.domain.model.UserModel;
import com.example.vehicle_auction.domain.repository.UserRepository;
import com.example.vehicle_auction.infrastructure.persistence.entity.Role;
import com.example.vehicle_auction.infrastructure.persistence.mapper.UserEntityMapper;
import com.example.vehicle_auction.infrastructure.persistence.entity.User;
import lombok.RequiredArgsConstructor;
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
    private final UserEntityMapper userEntityMapper;

    @Override
    public UserModel save(UserModel userModel) {
        User entity = userEntityMapper.toEntity(userModel);

        if (entity.getAccount() != null && userModel.getAccount() != null && userModel.getAccount().getRoles() != null) {
            Set<Role> managedRoles = userModel.getAccount().getRoles().stream()
                    .map(roleModel -> jpaRoleRepository.getReferenceById(roleModel.getId()))
                    .collect(Collectors.toSet());

            entity.getAccount().setRoles(managedRoles);
        }

        User savedEntity = jpaUserRepository.save(entity);
        return userEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<UserModel> findByAccountId(UUID accountId) {
        return jpaUserRepository.findByAccountId(accountId)
                .map(userEntityMapper::toDomain);
    }
}
