package com.example.vehicle_auction.infrastructure.persistence.repository;

import com.example.vehicle_auction.domain.model.UserModel;
import com.example.vehicle_auction.domain.repository.UserRepository;
import com.example.vehicle_auction.infrastructure.persistence.mapper.UserEntityMapper;
import com.example.vehicle_auction.infrastructure.persistence.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository jpaUserRepository;
    private final UserEntityMapper userEntityMapper;

    @Override
    public UserModel save(UserModel userModel) {
        User entity = userEntityMapper.toEntity(userModel);
        User savedEntity = jpaUserRepository.save(entity);
        return userEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<UserModel> findByAccountId(UUID accountId) {
        return jpaUserRepository.findByAccountId(accountId)
                .map(userEntityMapper::toDomain);
    }
}
