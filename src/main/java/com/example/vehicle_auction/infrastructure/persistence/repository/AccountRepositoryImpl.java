package com.example.vehicle_auction.infrastructure.persistence.repository;

import com.example.vehicle_auction.domain.model.AccountModel;
import com.example.vehicle_auction.domain.repository.AccountRepository;
import com.example.vehicle_auction.infrastructure.persistence.mapper.AccountEntityMapper;
import com.example.vehicle_auction.infrastructure.persistence.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {
    private final JpaAccountRepository jpaAccountRepository;
    private final AccountEntityMapper accountEntityMapper;

    @Override
    public boolean existsByEmail(String email) {
        return jpaAccountRepository.existsByEmail(email);
    }

    @Override
    public AccountModel save(AccountModel accountModel) {
        if (accountModel.getId() != null && jpaAccountRepository.existsById(accountModel.getId())) {
            Account existingEntity = jpaAccountRepository.findById(accountModel.getId()).get();
            accountEntityMapper.updateEntityFromModel(accountModel, existingEntity);

            return accountEntityMapper.toDomain(jpaAccountRepository.save(existingEntity));
        } else {
            Account newEntity = accountEntityMapper.toEntity(accountModel);
            return accountEntityMapper.toDomain(jpaAccountRepository.save(newEntity));
        }
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
