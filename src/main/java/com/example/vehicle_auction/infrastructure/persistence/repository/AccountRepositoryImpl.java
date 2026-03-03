package com.example.vehicle_auction.infrastructure.persistence.repository;

import com.example.vehicle_auction.domain.model.AccountModel;
import com.example.vehicle_auction.domain.repository.AccountRepository;
import com.example.vehicle_auction.infrastructure.persistence.entity.Role;
import com.example.vehicle_auction.infrastructure.persistence.mapper.AccountEntityMapper;
import com.example.vehicle_auction.infrastructure.persistence.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {
    private final JpaAccountRepository jpaAccountRepository;
    private final JpaRoleRepository jpaRoleRepository;
    private final AccountEntityMapper accountEntityMapper;

    @Override
    public boolean existsByEmail(String email) {
        return jpaAccountRepository.existsByEmail(email);
    }

    @Override
    public AccountModel save(AccountModel accountModel) {
        Account entity;

        if (accountModel.getId() != null && jpaAccountRepository.existsById(accountModel.getId())) {
            entity = jpaAccountRepository.findById(accountModel.getId()).get();
            accountEntityMapper.updateEntityFromModel(accountModel, entity);
        } else {
            entity = accountEntityMapper.toEntity(accountModel);
        }

        if (accountModel.getRoles() != null) {
            Set<Role> managedRoles = accountModel.getRoles().stream()
                    .map(roleModel -> jpaRoleRepository.getReferenceById(roleModel.getId()))
                    .collect(Collectors.toSet());

            entity.setRoles(managedRoles);
        }

        Account savedEntity = jpaAccountRepository.save(entity);
        return accountEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<AccountModel> findByEmail(String email) {
        return jpaAccountRepository.findByEmail(email)
                .map(accountEntityMapper::toDomain);
    }

    @Override
    public Optional<AccountModel> findById(UUID id) {
        return jpaAccountRepository.findById(id)
                .map(accountEntityMapper::toDomain);
    }
}
