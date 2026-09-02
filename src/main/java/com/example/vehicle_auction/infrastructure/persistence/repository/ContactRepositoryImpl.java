package com.example.vehicle_auction.infrastructure.persistence.repository;

import com.example.vehicle_auction.domain.model.ContactModel;
import com.example.vehicle_auction.domain.repository.ContactRepository;
import com.example.vehicle_auction.infrastructure.persistence.entity.Contact;
import com.example.vehicle_auction.infrastructure.persistence.mapper.ContactEntityMapper;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ContactRepositoryImpl implements ContactRepository {
    private final JpaContactRepository jpaContactRepository;
    private final ContactEntityMapper contactEntityMapper;

    @Override
    public ContactModel save(ContactModel contact) {
        Contact entity = contactEntityMapper.toEntity(contact);
        return contactEntityMapper.toDomain(jpaContactRepository.save(entity));
    }

    @Override
    public Page<ContactModel> findAllByDeletedFalse(Pageable pageable) {
        return jpaContactRepository.findAllByDeletedFalse(pageable)
                .map(contactEntityMapper::toDomain);
    }

    @Override
    public Page<ContactModel> findAllByDeletedTrue(Pageable pageable) {
        return jpaContactRepository.findAllByDeletedTrue(pageable)
                .map(contactEntityMapper::toDomain);
    }

    @Override
    public Optional<ContactModel> findById(UUID id) {
        return jpaContactRepository.findById(id)
                .map(contactEntityMapper::toDomain);
    }

    @Override
    public Optional<ContactModel> findByPhoneNumber(String phoneNumber) {
        return jpaContactRepository.findByPhoneNumber(phoneNumber)
                .map(contactEntityMapper::toDomain);
    }

    @Override
    public Optional<ContactModel> findByEmail(String email) {
        return jpaContactRepository.findByEmail(email)
                .map(contactEntityMapper::toDomain);
    }
}
