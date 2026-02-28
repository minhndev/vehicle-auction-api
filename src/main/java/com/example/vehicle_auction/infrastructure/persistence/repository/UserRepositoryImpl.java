package com.example.vehicle_auction.infrastructure.persistence.repository;

import com.example.vehicle_auction.domain.model.UserModel;
import com.example.vehicle_auction.domain.repository.UserRepository;
import com.example.vehicle_auction.infrastructure.persistence.mapper.UserMapper;
import com.example.vehicle_auction.infrastructure.persistence.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;

    @Override
    public UserModel save(UserModel userModel) {
        User entity = userMapper.toEntity(userModel);
        User savedEntity = jpaUserRepository.save(entity);
        return userMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<UserModel> findByAccountId(UUID accountId) {
        return jpaUserRepository.findByAccountId(accountId)
                .map(userMapper::toDomain);
    }
}
