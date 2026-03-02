package com.example.vehicle_auction.infrastructure.persistence.repository;

import com.example.vehicle_auction.domain.model.AccountModel;
import com.example.vehicle_auction.domain.repository.AccountRepository;
import com.example.vehicle_auction.infrastructure.persistence.mapper.AccountMapper;
import com.example.vehicle_auction.infrastructure.persistence.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {
    private final JpaAccountRepository jpaAccountRepository;
    private final AccountMapper accountMapper;

    @Override
    public boolean existsByEmail(String email) {
        return jpaAccountRepository.existsByEmail(email);
    }

    @Override
    public AccountModel save(AccountModel accountModel) {
        if (accountModel.getId() != null && jpaAccountRepository.existsById(accountModel.getId())) {
            Account existingEntity = jpaAccountRepository.findById(accountModel.getId()).get();
            accountMapper.updateEntityFromModel(accountModel, existingEntity);

            return accountMapper.toDomain(jpaAccountRepository.save(existingEntity));
        } else {
            Account newEntity = accountMapper.toEntity(accountModel);
            return accountMapper.toDomain(jpaAccountRepository.save(newEntity));
        }
    }

    @Override
    public Optional<AccountModel> findByEmail(String email) {
        return jpaAccountRepository.findByEmail(email)
                .map(accountMapper::toDomain);
    }

    @Override
    public Optional<AccountModel> findById(UUID id) {
        return jpaAccountRepository.findById(id)
                .map(accountMapper::toDomain);
    }
}
